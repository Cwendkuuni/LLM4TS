package weka.classifiers;

import weka.estimators.*;
import weka.classifiers.pmml.consumer.*;
import weka.classifiers.xml.*;
import weka.core.converters.*;
import weka.classifiers.misc.*;
import weka.classifiers.evaluation.output.prediction.*;
import java.util.zip.*;
import weka.core.pmml.*;
import weka.core.xml.*;
import java.io.*;
import weka.classifiers.evaluation.*;
import weka.experiment.*;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;
import weka.core.*;

public class Evaluation implements Summarizable, RevisionHandler, Serializable
{
    private static final long serialVersionUID = -7010314486866816271L;
    protected int m_NumClasses;
    protected int m_NumFolds;
    protected double m_Incorrect;
    protected double m_Correct;
    protected double m_Unclassified;
    protected double m_MissingClass;
    protected double m_WithClass;
    protected double[][] m_ConfusionMatrix;
    protected String[] m_ClassNames;
    protected boolean m_ClassIsNominal;
    protected double[] m_ClassPriors;
    protected double m_ClassPriorsSum;
    protected CostMatrix m_CostMatrix;
    protected double m_TotalCost;
    protected double m_SumErr;
    protected double m_SumAbsErr;
    protected double m_SumSqrErr;
    protected double m_SumClass;
    protected double m_SumSqrClass;
    protected double m_SumPredicted;
    protected double m_SumSqrPredicted;
    protected double m_SumClassPredicted;
    protected double m_SumPriorAbsErr;
    protected double m_SumPriorSqrErr;
    protected double m_SumKBInfo;
    protected static int k_MarginResolution;
    protected double[] m_MarginCounts;
    protected int m_NumTrainClassVals;
    protected double[] m_TrainClassVals;
    protected double[] m_TrainClassWeights;
    protected UnivariateKernelEstimator m_PriorEstimator;
    protected boolean m_ComplexityStatisticsAvailable;
    protected static final double MIN_SF_PROB = Double.MIN_VALUE;
    protected double m_SumPriorEntropy;
    protected double m_SumSchemeEntropy;
    protected boolean m_CoverageStatisticsAvailable;
    protected double m_ConfLevel;
    protected double m_TotalSizeOfRegions;
    protected double m_TotalCoverage;
    protected double m_MinTarget;
    protected double m_MaxTarget;
    protected FastVector m_Predictions;
    protected boolean m_NoPriors;
    protected Instances m_Header;
    protected boolean m_DiscardPredictions;
    
    public Evaluation(final Instances data) throws Exception {
        this(data, null);
    }
    
    public Evaluation(final Instances data, final CostMatrix costMatrix) throws Exception {
        this.m_ComplexityStatisticsAvailable = true;
        this.m_CoverageStatisticsAvailable = true;
        this.m_ConfLevel = 0.95;
        this.m_NoPriors = false;
        this.m_Header = new Instances(data, 0);
        this.m_NumClasses = data.numClasses();
        this.m_NumFolds = 1;
        this.m_ClassIsNominal = data.classAttribute().isNominal();
        if (this.m_ClassIsNominal) {
            this.m_ConfusionMatrix = new double[this.m_NumClasses][this.m_NumClasses];
            this.m_ClassNames = new String[this.m_NumClasses];
            for (int i = 0; i < this.m_NumClasses; ++i) {
                this.m_ClassNames[i] = data.classAttribute().value(i);
            }
        }
        this.m_CostMatrix = costMatrix;
        if (this.m_CostMatrix != null) {
            if (!this.m_ClassIsNominal) {
                throw new Exception("Class has to be nominal if cost matrix given!");
            }
            if (this.m_CostMatrix.size() != this.m_NumClasses) {
                throw new Exception("Cost matrix not compatible with data!");
            }
        }
        this.m_ClassPriors = new double[this.m_NumClasses];
        this.setPriors(data);
        this.m_MarginCounts = new double[Evaluation.k_MarginResolution + 1];
    }
    
    public Instances getHeader() {
        return this.m_Header;
    }
    
    public void setDiscardPredictions(final boolean value) {
        this.m_DiscardPredictions = value;
        if (this.m_DiscardPredictions) {
            this.m_Predictions = null;
        }
    }
    
    public boolean getDiscardPredictions() {
        return this.m_DiscardPredictions;
    }
    
    public double areaUnderROC(final int classIndex) {
        if (this.m_Predictions == null) {
            return Utils.missingValue();
        }
        final ThresholdCurve tc = new ThresholdCurve();
        final Instances result = tc.getCurve(this.m_Predictions, classIndex);
        return ThresholdCurve.getROCArea(result);
    }
    
    public double weightedAreaUnderROC() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double aucTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.areaUnderROC(k);
            if (!Utils.isMissingValue(temp)) {
                aucTotal += temp * classCounts[k];
            }
        }
        return aucTotal / classCountSum;
    }
    
    public double areaUnderPRC(final int classIndex) {
        if (this.m_Predictions == null) {
            return Utils.missingValue();
        }
        final ThresholdCurve tc = new ThresholdCurve();
        final Instances result = tc.getCurve(this.m_Predictions, classIndex);
        return ThresholdCurve.getPRCArea(result);
    }
    
    public double weightedAreaUnderPRC() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double auprcTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.areaUnderPRC(k);
            if (!Utils.isMissingValue(temp)) {
                auprcTotal += temp * classCounts[k];
            }
        }
        return auprcTotal / classCountSum;
    }
    
    public double[][] confusionMatrix() {
        final double[][] newMatrix = new double[this.m_ConfusionMatrix.length][0];
        for (int i = 0; i < this.m_ConfusionMatrix.length; ++i) {
            newMatrix[i] = new double[this.m_ConfusionMatrix[i].length];
            System.arraycopy(this.m_ConfusionMatrix[i], 0, newMatrix[i], 0, this.m_ConfusionMatrix[i].length);
        }
        return newMatrix;
    }
    
    public void crossValidateModel(final Classifier classifier, Instances data, final int numFolds, final Random random, final Object... forPredictionsPrinting) throws Exception {
        data = new Instances(data);
        data.randomize(random);
        if (data.classAttribute().isNominal()) {
            data.stratify(numFolds);
        }
        AbstractOutput classificationOutput = null;
        if (forPredictionsPrinting.length > 0) {
            classificationOutput = (AbstractOutput)forPredictionsPrinting[0];
            classificationOutput.setHeader(data);
            classificationOutput.printHeader();
        }
        for (int i = 0; i < numFolds; ++i) {
            final Instances train = data.trainCV(numFolds, i, random);
            this.setPriors(train);
            final Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
            copiedClassifier.buildClassifier(train);
            final Instances test = data.testCV(numFolds, i);
            this.evaluateModel(copiedClassifier, test, forPredictionsPrinting);
        }
        this.m_NumFolds = numFolds;
        if (classificationOutput != null) {
            classificationOutput.printFooter();
        }
    }
    
    public void crossValidateModel(final String classifierString, final Instances data, final int numFolds, final String[] options, final Random random) throws Exception {
        this.crossValidateModel(AbstractClassifier.forName(classifierString, options), data, numFolds, random, new Object[0]);
    }
    
    public static String evaluateModel(final String classifierString, final String[] options) throws Exception {
        Classifier classifier;
        try {
            classifier = AbstractClassifier.forName(classifierString, null);
        }
        catch (Exception e) {
            throw new Exception("Can't find class with name " + classifierString + '.');
        }
        return evaluateModel(classifier, options);
    }
    
    public static void main(final String[] args) {
        try {
            if (args.length == 0) {
                throw new Exception("The first argument must be the class name of a classifier");
            }
            final String classifier = args[0];
            args[0] = "";
            System.out.println(evaluateModel(classifier, args));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
    
    public static String evaluateModel(Classifier classifier, String[] options) throws Exception {
        Instances train = null;
        Instances test = null;
        Instances template = null;
        int seed = 1;
        int folds = 10;
        int classIndex = -1;
        boolean noCrossValidation = false;
        boolean noOutput = false;
        boolean trainStatistics = true;
        boolean printMargins = false;
        boolean printComplexityStatistics = false;
        boolean printGraph = false;
        boolean classStatistics = false;
        boolean printSource = false;
        final StringBuffer text = new StringBuffer();
        ConverterUtils.DataSource trainSource = null;
        ConverterUtils.DataSource testSource = null;
        ObjectInputStream objectInputStream = null;
        BufferedInputStream xmlInputStream = null;
        CostMatrix costMatrix = null;
        StringBuffer schemeOptionsText = null;
        long trainTimeStart = 0L;
        long trainTimeElapsed = 0L;
        long testTimeStart = 0L;
        long testTimeElapsed = 0L;
        String xml = "";
        String[] optionsTmp = null;
        Classifier classifierClassifications = null;
        int actualClassIndex = -1;
        String splitPercentageString = "";
        double splitPercentage = -1.0;
        boolean preserveOrder = false;
        boolean trainSetPresent = false;
        boolean testSetPresent = false;
        boolean discardPredictions = false;
        StringBuffer predsBuff = null;
        AbstractOutput classificationOutput = null;
        if (Utils.getFlag("h", options) || Utils.getFlag("help", options)) {
            final boolean globalInfo = Utils.getFlag("synopsis", options) || Utils.getFlag("info", options);
            throw new Exception("\nHelp requested." + makeOptionString(classifier, globalInfo));
        }
        String objectInputFileName;
        String objectOutputFileName;
        String sourceClass;
        String thresholdFile;
        String thresholdLabel;
        try {
            xml = Utils.getOption("xml", options);
            if (!xml.equals("")) {
                options = new XMLOptions(xml).toArray();
            }
            optionsTmp = new String[options.length];
            for (int i = 0; i < options.length; ++i) {
                optionsTmp[i] = options[i];
            }
            final String tmpO = Utils.getOption('l', optionsTmp);
            if (tmpO.endsWith(".xml")) {
                boolean success = false;
                try {
                    final PMMLModel pmmlModel = PMMLFactory.getPMMLModel(tmpO);
                    if (pmmlModel instanceof PMMLClassifier) {
                        classifier = (PMMLClassifier)pmmlModel;
                        success = true;
                    }
                }
                catch (IllegalArgumentException ex) {
                    success = false;
                }
                if (!success) {
                    final XMLClassifier xmlserial = new XMLClassifier();
                    final OptionHandler cl = (OptionHandler)xmlserial.read(Utils.getOption('l', options));
                    optionsTmp = new String[options.length + cl.getOptions().length];
                    System.arraycopy(cl.getOptions(), 0, optionsTmp, 0, cl.getOptions().length);
                    System.arraycopy(options, 0, optionsTmp, cl.getOptions().length, options.length);
                    options = optionsTmp;
                }
            }
            noCrossValidation = Utils.getFlag("no-cv", options);
            final String classIndexString = Utils.getOption('c', options);
            if (classIndexString.length() != 0) {
                if (classIndexString.equals("first")) {
                    classIndex = 1;
                }
                else if (classIndexString.equals("last")) {
                    classIndex = -1;
                }
                else {
                    classIndex = Integer.parseInt(classIndexString);
                }
            }
            final String trainFileName = Utils.getOption('t', options);
            objectInputFileName = Utils.getOption('l', options);
            objectOutputFileName = Utils.getOption('d', options);
            final String testFileName = Utils.getOption('T', options);
            final String foldsString = Utils.getOption('x', options);
            if (foldsString.length() != 0) {
                folds = Integer.parseInt(foldsString);
            }
            final String seedString = Utils.getOption('s', options);
            if (seedString.length() != 0) {
                seed = Integer.parseInt(seedString);
            }
            if (trainFileName.length() == 0) {
                if (objectInputFileName.length() == 0) {
                    throw new Exception("No training file and no object input file given.");
                }
                if (testFileName.length() == 0) {
                    throw new Exception("No training file and no test file given.");
                }
            }
            else if (objectInputFileName.length() != 0 && (!(classifier instanceof UpdateableClassifier) || testFileName.length() == 0)) {
                throw new Exception("Classifier not incremental, or no test file provided: can't use both train and model file.");
            }
            try {
                if (trainFileName.length() != 0) {
                    trainSetPresent = true;
                    trainSource = new ConverterUtils.DataSource(trainFileName);
                }
                if (testFileName.length() != 0) {
                    testSetPresent = true;
                    testSource = new ConverterUtils.DataSource(testFileName);
                }
                if (objectInputFileName.length() != 0) {
                    if (objectInputFileName.endsWith(".xml")) {
                        objectInputStream = null;
                        xmlInputStream = null;
                    }
                    else {
                        InputStream is = new FileInputStream(objectInputFileName);
                        if (objectInputFileName.endsWith(".gz")) {
                            is = new GZIPInputStream(is);
                        }
                        if (!objectInputFileName.endsWith(".koml") || !KOML.isPresent()) {
                            objectInputStream = new ObjectInputStream(is);
                            xmlInputStream = null;
                        }
                        else {
                            objectInputStream = null;
                            xmlInputStream = new BufferedInputStream(is);
                        }
                    }
                }
            }
            catch (Exception e) {
                throw new Exception("Can't open file " + e.getMessage() + '.');
            }
            if (testSetPresent) {
                test = (template = testSource.getStructure());
                if (classIndex != -1) {
                    test.setClassIndex(classIndex - 1);
                }
                else if (test.classIndex() == -1 || classIndexString.length() != 0) {
                    test.setClassIndex(test.numAttributes() - 1);
                }
                actualClassIndex = test.classIndex();
            }
            else {
                splitPercentageString = Utils.getOption("split-percentage", options);
                if (splitPercentageString.length() != 0) {
                    if (foldsString.length() != 0) {
                        throw new Exception("Percentage split cannot be used in conjunction with cross-validation ('-x').");
                    }
                    splitPercentage = Double.parseDouble(splitPercentageString);
                    if (splitPercentage <= 0.0 || splitPercentage >= 100.0) {
                        throw new Exception("Percentage split value needs be >0 and <100.");
                    }
                }
                else {
                    splitPercentage = -1.0;
                }
                preserveOrder = Utils.getFlag("preserve-order", options);
                if (preserveOrder && splitPercentage == -1.0) {
                    throw new Exception("Percentage split ('-percentage-split') is missing.");
                }
                if (splitPercentage > 0.0) {
                    testSetPresent = true;
                    final Instances tmpInst = trainSource.getDataSet(actualClassIndex);
                    if (!preserveOrder) {
                        tmpInst.randomize(new Random(seed));
                    }
                    final int trainSize = (int)Math.round(tmpInst.numInstances() * splitPercentage / 100.0);
                    final int testSize = tmpInst.numInstances() - trainSize;
                    final Instances trainInst = new Instances(tmpInst, 0, trainSize);
                    final Instances testInst = new Instances(tmpInst, trainSize, testSize);
                    trainSource = new ConverterUtils.DataSource(trainInst);
                    testSource = new ConverterUtils.DataSource(testInst);
                    test = (template = testSource.getStructure());
                    if (classIndex != -1) {
                        test.setClassIndex(classIndex - 1);
                    }
                    else if (test.classIndex() == -1 || classIndexString.length() != 0) {
                        test.setClassIndex(test.numAttributes() - 1);
                    }
                    actualClassIndex = test.classIndex();
                }
            }
            if (trainSetPresent) {
                train = (template = trainSource.getStructure());
                if (classIndex != -1) {
                    train.setClassIndex(classIndex - 1);
                }
                else if (train.classIndex() == -1 || classIndexString.length() != 0) {
                    train.setClassIndex(train.numAttributes() - 1);
                }
                actualClassIndex = train.classIndex();
                if (!(classifier instanceof InputMappedClassifier) && testSetPresent && !test.equalHeaders(train)) {
                    throw new IllegalArgumentException("Train and test file not compatible!\n" + test.equalHeadersMsg(train));
                }
            }
            if (template == null) {
                throw new Exception("No actual dataset provided to use as template");
            }
            costMatrix = handleCostOption(Utils.getOption('m', options), template.numClasses());
            classStatistics = Utils.getFlag('i', options);
            noOutput = Utils.getFlag('o', options);
            trainStatistics = !Utils.getFlag('v', options);
            printComplexityStatistics = Utils.getFlag('k', options);
            printMargins = Utils.getFlag('r', options);
            printGraph = Utils.getFlag('g', options);
            sourceClass = Utils.getOption('z', options);
            printSource = (sourceClass.length() != 0);
            thresholdFile = Utils.getOption("threshold-file", options);
            thresholdLabel = Utils.getOption("threshold-label", options);
            final String classifications = Utils.getOption("classifications", options);
            final String classificationsOld = Utils.getOption("p", options);
            if (classifications.length() > 0) {
                noOutput = true;
                classificationOutput = AbstractOutput.fromCommandline(classifications);
                if (classificationOutput == null) {
                    throw new Exception("Failed to instantiate class for classification output: " + classifications);
                }
                classificationOutput.setHeader(template);
            }
            else if (classificationsOld.length() > 0) {
                noOutput = true;
                classificationOutput = new PlainText();
                classificationOutput.setHeader(template);
                if (!classificationsOld.equals("0")) {
                    classificationOutput.setAttributes(classificationsOld);
                }
                classificationOutput.setOutputDistribution(Utils.getFlag("distribution", options));
            }
            else if (Utils.getFlag("distribution", options)) {
                throw new Exception("Cannot print distribution without '-p' option!");
            }
            discardPredictions = Utils.getFlag("no-predictions", options);
            if (discardPredictions && classificationOutput != null) {
                throw new Exception("Cannot discard predictions ('-no-predictions') and output predictions at the same time ('-classifications/-p')!");
            }
            if (!trainSetPresent && printComplexityStatistics) {
                throw new Exception("Cannot print complexity statistics ('-k') without training file ('-t')!");
            }
            if (objectInputFileName.length() != 0) {
                Utils.checkForRemainingOptions(options);
            }
            else if (classifier instanceof OptionHandler) {
                for (int j = 0; j < options.length; ++j) {
                    if (options[j].length() != 0) {
                        if (schemeOptionsText == null) {
                            schemeOptionsText = new StringBuffer();
                        }
                        if (options[j].indexOf(32) != -1) {
                            schemeOptionsText.append('\"' + options[j] + "\" ");
                        }
                        else {
                            schemeOptionsText.append(options[j] + " ");
                        }
                    }
                }
                ((OptionHandler)classifier).setOptions(options);
            }
            Utils.checkForRemainingOptions(options);
        }
        catch (Exception e2) {
            throw new Exception("\nWeka exception: " + e2.getMessage() + makeOptionString(classifier, false));
        }
        if (objectInputFileName.length() != 0) {
            if (objectInputStream != null) {
                classifier = (Classifier)objectInputStream.readObject();
                Instances savedStructure = null;
                try {
                    savedStructure = (Instances)objectInputStream.readObject();
                }
                catch (Exception ex2) {}
                if (savedStructure != null && !template.equalHeaders(savedStructure)) {
                    throw new Exception("training and test set are not compatible\n" + template.equalHeadersMsg(savedStructure));
                }
                objectInputStream.close();
            }
            else if (xmlInputStream != null) {
                classifier = (Classifier)KOML.read(xmlInputStream);
                xmlInputStream.close();
            }
        }
        Evaluation trainingEvaluation = new Evaluation(new Instances(template, 0), costMatrix);
        Evaluation testingEvaluation = new Evaluation(new Instances(template, 0), costMatrix);
        if (classifier instanceof InputMappedClassifier) {
            final Instances mappedClassifierHeader = ((InputMappedClassifier)classifier).getModelHeader(new Instances(template, 0));
            trainingEvaluation = new Evaluation(new Instances(mappedClassifierHeader, 0), costMatrix);
            testingEvaluation = new Evaluation(new Instances(mappedClassifierHeader, 0), costMatrix);
        }
        trainingEvaluation.setDiscardPredictions(discardPredictions);
        testingEvaluation.setDiscardPredictions(discardPredictions);
        if (!trainSetPresent) {
            testingEvaluation.useNoPriors();
        }
        final Classifier classifierBackup = AbstractClassifier.makeCopy(classifier);
        if (classifier instanceof UpdateableClassifier && (testSetPresent || noCrossValidation) && costMatrix == null && trainSetPresent) {
            trainingEvaluation.setPriors(train);
            testingEvaluation.setPriors(train);
            trainTimeStart = System.currentTimeMillis();
            if (objectInputFileName.length() == 0) {
                classifier.buildClassifier(train);
            }
            while (trainSource.hasMoreElements(train)) {
                final Instance trainInst2 = trainSource.nextElement(train);
                trainingEvaluation.updatePriors(trainInst2);
                testingEvaluation.updatePriors(trainInst2);
                ((UpdateableClassifier)classifier).updateClassifier(trainInst2);
            }
            trainTimeElapsed = System.currentTimeMillis() - trainTimeStart;
        }
        else if (objectInputFileName.length() == 0) {
            Instances tempTrain = trainSource.getDataSet(actualClassIndex);
            if (classifier instanceof InputMappedClassifier && !trainingEvaluation.getHeader().equalHeaders(tempTrain)) {
                final Instances mappedClassifierDataset = ((InputMappedClassifier)classifier).getModelHeader(new Instances(template, 0));
                for (int zz = 0; zz < tempTrain.numInstances(); ++zz) {
                    final Instance mapped = ((InputMappedClassifier)classifier).constructMappedInstance(tempTrain.instance(zz));
                    mappedClassifierDataset.add(mapped);
                }
                tempTrain = mappedClassifierDataset;
            }
            trainingEvaluation.setPriors(tempTrain);
            testingEvaluation.setPriors(tempTrain);
            trainTimeStart = System.currentTimeMillis();
            classifier.buildClassifier(tempTrain);
            trainTimeElapsed = System.currentTimeMillis() - trainTimeStart;
        }
        if (classificationOutput != null) {
            classifierClassifications = AbstractClassifier.makeCopy(classifier);
            if (classifier instanceof InputMappedClassifier) {
                classificationOutput.setHeader(trainingEvaluation.getHeader());
            }
        }
        if (objectOutputFileName.length() != 0) {
            OutputStream os = new FileOutputStream(objectOutputFileName);
            if (!objectOutputFileName.endsWith(".xml") && (!objectOutputFileName.endsWith(".koml") || !KOML.isPresent())) {
                if (objectOutputFileName.endsWith(".gz")) {
                    os = new GZIPOutputStream(os);
                }
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
                objectOutputStream.writeObject(classifier);
                if (template != null) {
                    objectOutputStream.writeObject(template);
                }
                objectOutputStream.flush();
                objectOutputStream.close();
            }
            else {
                final BufferedOutputStream xmlOutputStream = new BufferedOutputStream(os);
                if (objectOutputFileName.endsWith(".xml")) {
                    final XMLSerialization xmlSerial = new XMLClassifier();
                    xmlSerial.write(xmlOutputStream, classifier);
                }
                else if (objectOutputFileName.endsWith(".koml")) {
                    KOML.write(xmlOutputStream, classifier);
                }
                xmlOutputStream.close();
            }
        }
        if (classifier instanceof Drawable && printGraph) {
            return ((Drawable)classifier).graph();
        }
        if (classifier instanceof Sourcable && printSource) {
            return wekaStaticWrapper((Sourcable)classifier, sourceClass);
        }
        if (!noOutput && !printMargins) {
            if (classifier instanceof OptionHandler && schemeOptionsText != null) {
                text.append("\nOptions: " + (Object)schemeOptionsText);
                text.append("\n");
            }
            text.append("\n" + classifier.toString() + "\n");
        }
        if (!printMargins && costMatrix != null) {
            text.append("\n=== Evaluation Cost Matrix ===\n\n");
            text.append(costMatrix.toString());
        }
        if (classificationOutput != null) {
            ConverterUtils.DataSource source = testSource;
            predsBuff = new StringBuffer();
            classificationOutput.setBuffer(predsBuff);
            if (source == null && noCrossValidation) {
                source = trainSource;
                predsBuff.append("\n=== Predictions on training data ===\n\n");
            }
            else {
                predsBuff.append("\n=== Predictions on test data ===\n\n");
            }
            if (source != null) {
                classificationOutput.print(classifierClassifications, source);
            }
        }
        if (trainStatistics && trainSetPresent) {
            if (classifier instanceof UpdateableClassifier && testSetPresent && costMatrix == null) {
                trainSource.reset();
                train = trainSource.getStructure(actualClassIndex);
                testTimeStart = System.currentTimeMillis();
                while (trainSource.hasMoreElements(train)) {
                    final Instance trainInst2 = trainSource.nextElement(train);
                    trainingEvaluation.evaluateModelOnce(classifier, trainInst2);
                }
                testTimeElapsed = System.currentTimeMillis() - testTimeStart;
            }
            else {
                testTimeStart = System.currentTimeMillis();
                trainingEvaluation.evaluateModel(classifier, trainSource.getDataSet(actualClassIndex), new Object[0]);
                testTimeElapsed = System.currentTimeMillis() - testTimeStart;
            }
            if (printMargins) {
                return trainingEvaluation.toCumulativeMarginDistributionString();
            }
            if (classificationOutput == null) {
                text.append("\nTime taken to build model: " + Utils.doubleToString(trainTimeElapsed / 1000.0, 2) + " seconds");
                if (splitPercentage > 0.0) {
                    text.append("\nTime taken to test model on training split: ");
                }
                else {
                    text.append("\nTime taken to test model on training data: ");
                }
                text.append(Utils.doubleToString(testTimeElapsed / 1000.0, 2) + " seconds");
                if (splitPercentage > 0.0) {
                    text.append(trainingEvaluation.toSummaryString("\n\n=== Error on training split ===\n", printComplexityStatistics));
                }
                else {
                    text.append(trainingEvaluation.toSummaryString("\n\n=== Error on training data ===\n", printComplexityStatistics));
                }
                if (template.classAttribute().isNominal()) {
                    if (classStatistics) {
                        text.append("\n\n" + trainingEvaluation.toClassDetailsString());
                    }
                    if (!noCrossValidation) {
                        text.append("\n\n" + trainingEvaluation.toMatrixString());
                    }
                }
            }
        }
        if (testSource != null) {
            testSource.reset();
            test = testSource.getStructure(test.classIndex());
            while (testSource.hasMoreElements(test)) {
                final Instance testInst2 = testSource.nextElement(test);
                testingEvaluation.evaluateModelOnceAndRecordPrediction(classifier, testInst2);
            }
            if (splitPercentage > 0.0) {
                if (classificationOutput == null) {
                    text.append("\n\n" + testingEvaluation.toSummaryString("=== Error on test split ===\n", printComplexityStatistics));
                }
            }
            else if (classificationOutput == null) {
                text.append("\n\n" + testingEvaluation.toSummaryString("=== Error on test data ===\n", printComplexityStatistics));
            }
        }
        else if (trainSource != null && !noCrossValidation) {
            final Random random = new Random(seed);
            classifier = AbstractClassifier.makeCopy(classifierBackup);
            if (classificationOutput == null) {
                testingEvaluation.crossValidateModel(classifier, trainSource.getDataSet(actualClassIndex), folds, random, new Object[0]);
                if (template.classAttribute().isNumeric()) {
                    text.append("\n\n\n" + testingEvaluation.toSummaryString("=== Cross-validation ===\n", printComplexityStatistics));
                }
                else {
                    text.append("\n\n\n" + testingEvaluation.toSummaryString("=== Stratified cross-validation ===\n", printComplexityStatistics));
                }
            }
            else {
                predsBuff = new StringBuffer();
                classificationOutput.setBuffer(predsBuff);
                predsBuff.append("\n=== Predictions under cross-validation ===\n\n");
                testingEvaluation.crossValidateModel(classifier, trainSource.getDataSet(actualClassIndex), folds, random, classificationOutput);
            }
        }
        if (template.classAttribute().isNominal()) {
            if (classStatistics && !noCrossValidation && classificationOutput == null) {
                text.append("\n\n" + testingEvaluation.toClassDetailsString());
            }
            if (!noCrossValidation && classificationOutput == null) {
                text.append("\n\n" + testingEvaluation.toMatrixString());
            }
        }
        if (predsBuff != null) {
            text.append("\n" + (Object)predsBuff);
        }
        if (thresholdFile.length() != 0 && template.classAttribute().isNominal()) {
            int labelIndex = 0;
            if (thresholdLabel.length() != 0) {
                labelIndex = template.classAttribute().indexOfValue(thresholdLabel);
            }
            if (labelIndex == -1) {
                throw new IllegalArgumentException("Class label '" + thresholdLabel + "' is unknown!");
            }
            final ThresholdCurve tc = new ThresholdCurve();
            final Instances result = tc.getCurve(testingEvaluation.predictions(), labelIndex);
            ConverterUtils.DataSink.write(thresholdFile, result);
        }
        return text.toString();
    }
    
    protected static CostMatrix handleCostOption(final String costFileName, final int numClasses) throws Exception {
        if (costFileName != null && costFileName.length() != 0) {
            System.out.println("NOTE: The behaviour of the -m option has changed between WEKA 3.0 and WEKA 3.1. -m now carries out cost-sensitive *evaluation* only. For cost-sensitive *prediction*, use one of the cost-sensitive metaschemes such as weka.classifiers.meta.CostSensitiveClassifier or weka.classifiers.meta.MetaCost");
            Reader costReader = null;
            try {
                costReader = new BufferedReader(new FileReader(costFileName));
            }
            catch (Exception e) {
                throw new Exception("Can't open file " + e.getMessage() + '.');
            }
            try {
                return new CostMatrix(costReader);
            }
            catch (Exception ex) {
                try {
                    try {
                        costReader.close();
                        costReader = new BufferedReader(new FileReader(costFileName));
                    }
                    catch (Exception e2) {
                        throw new Exception("Can't open file " + e2.getMessage() + '.');
                    }
                    final CostMatrix costMatrix = new CostMatrix(numClasses);
                    costMatrix.readOldFormat(costReader);
                    return costMatrix;
                }
                catch (Exception e3) {
                    throw ex;
                }
            }
        }
        return null;
    }
    
    public double[] evaluateModel(final Classifier classifier, final Instances data, final Object... forPredictionsPrinting) throws Exception {
        AbstractOutput classificationOutput = null;
        final double[] predictions = new double[data.numInstances()];
        if (forPredictionsPrinting.length > 0) {
            classificationOutput = (AbstractOutput)forPredictionsPrinting[0];
        }
        if (classifier instanceof BatchPredictor) {
            final double[][] preds = ((BatchPredictor)classifier).distributionsForInstances(data);
            for (int i = 0; i < data.numInstances(); ++i) {
                final double[] p = preds[i];
                predictions[i] = this.evaluationForSingleInstance(p, data.instance(i), true);
                if (classificationOutput != null) {
                    classificationOutput.printClassification(p, data.instance(i), i);
                }
            }
        }
        else {
            for (int j = 0; j < data.numInstances(); ++j) {
                predictions[j] = this.evaluateModelOnceAndRecordPrediction(classifier, data.instance(j));
                if (classificationOutput != null) {
                    classificationOutput.printClassification(classifier, data.instance(j), j);
                }
            }
        }
        return predictions;
    }
    
    public double evaluationForSingleInstance(final double[] dist, final Instance instance, final boolean storePredictions) throws Exception {
        double pred;
        if (this.m_ClassIsNominal) {
            pred = Utils.maxIndex(dist);
            if (dist[(int)pred] <= 0.0) {
                pred = Utils.missingValue();
            }
            this.updateStatsForClassifier(dist, instance);
            if (storePredictions && !this.m_DiscardPredictions) {
                if (this.m_Predictions == null) {
                    this.m_Predictions = new FastVector();
                }
                this.m_Predictions.addElement(new NominalPrediction(instance.classValue(), dist, instance.weight()));
            }
        }
        else {
            pred = dist[0];
            this.updateStatsForPredictor(pred, instance);
            if (storePredictions && !this.m_DiscardPredictions) {
                if (this.m_Predictions == null) {
                    this.m_Predictions = new FastVector();
                }
                this.m_Predictions.addElement(new NumericPrediction(instance.classValue(), pred, instance.weight()));
            }
        }
        return pred;
    }
    
    protected double evaluationForSingleInstance(final Classifier classifier, Instance instance, final boolean storePredictions) throws Exception {
        final Instance classMissing = (Instance)instance.copy();
        classMissing.setDataset(instance.dataset());
        if (classifier instanceof InputMappedClassifier) {
            instance = (Instance)instance.copy();
            instance = ((InputMappedClassifier)classifier).constructMappedInstance(instance);
            final int mappedClass = ((InputMappedClassifier)classifier).getMappedClassIndex();
            classMissing.setMissing(mappedClass);
        }
        else {
            classMissing.setClassMissing();
        }
        final double pred = this.evaluationForSingleInstance(classifier.distributionForInstance(classMissing), instance, storePredictions);
        if (!this.m_ClassIsNominal && !instance.classIsMissing() && !Utils.isMissingValue(pred)) {
            if (classifier instanceof IntervalEstimator) {
                this.updateStatsForIntervalEstimator((IntervalEstimator)classifier, classMissing, instance.classValue());
            }
            else {
                this.m_CoverageStatisticsAvailable = false;
            }
            if (classifier instanceof ConditionalDensityEstimator) {
                this.updateStatsForConditionalDensityEstimator((ConditionalDensityEstimator)classifier, classMissing, instance.classValue());
            }
            else {
                this.m_ComplexityStatisticsAvailable = false;
            }
        }
        return pred;
    }
    
    public double evaluateModelOnceAndRecordPrediction(final Classifier classifier, final Instance instance) throws Exception {
        return this.evaluationForSingleInstance(classifier, instance, true);
    }
    
    public double evaluateModelOnce(final Classifier classifier, final Instance instance) throws Exception {
        return this.evaluationForSingleInstance(classifier, instance, false);
    }
    
    public double evaluateModelOnce(final double[] dist, final Instance instance) throws Exception {
        return this.evaluationForSingleInstance(dist, instance, false);
    }
    
    public double evaluateModelOnceAndRecordPrediction(final double[] dist, final Instance instance) throws Exception {
        return this.evaluationForSingleInstance(dist, instance, true);
    }
    
    public void evaluateModelOnce(final double prediction, final Instance instance) throws Exception {
        this.evaluateModelOnce(this.makeDistribution(prediction), instance);
    }
    
    public FastVector predictions() {
        if (this.m_DiscardPredictions) {
            return null;
        }
        return this.m_Predictions;
    }
    
    public static String wekaStaticWrapper(final Sourcable classifier, final String className) throws Exception {
        final StringBuffer result = new StringBuffer();
        final String staticClassifier = classifier.toSource(className);
        result.append("// Generated with Weka " + Version.VERSION + "\n");
        result.append("//\n");
        result.append("// This code is public domain and comes with no warranty.\n");
        result.append("//\n");
        result.append("// Timestamp: " + new Date() + "\n");
        result.append("\n");
        result.append("package weka.classifiers;\n");
        result.append("\n");
        result.append("import weka.core.Attribute;\n");
        result.append("import weka.core.Capabilities;\n");
        result.append("import weka.core.Capabilities.Capability;\n");
        result.append("import weka.core.Instance;\n");
        result.append("import weka.core.Instances;\n");
        result.append("import weka.core.RevisionUtils;\n");
        result.append("import weka.classifiers.Classifier;\nimport weka.classifiers.AbstractClassifier;\n");
        result.append("\n");
        result.append("public class WekaWrapper\n");
        result.append("  extends AbstractClassifier {\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Returns only the toString() method.\n");
        result.append("   *\n");
        result.append("   * @return a string describing the classifier\n");
        result.append("   */\n");
        result.append("  public String globalInfo() {\n");
        result.append("    return toString();\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Returns the capabilities of this classifier.\n");
        result.append("   *\n");
        result.append("   * @return the capabilities\n");
        result.append("   */\n");
        result.append("  public Capabilities getCapabilities() {\n");
        result.append(((Classifier)classifier).getCapabilities().toSource("result", 4));
        result.append("    return result;\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * only checks the data against its capabilities.\n");
        result.append("   *\n");
        result.append("   * @param i the training data\n");
        result.append("   */\n");
        result.append("  public void buildClassifier(Instances i) throws Exception {\n");
        result.append("    // can classifier handle the data?\n");
        result.append("    getCapabilities().testWithFail(i);\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Classifies the given instance.\n");
        result.append("   *\n");
        result.append("   * @param i the instance to classify\n");
        result.append("   * @return the classification result\n");
        result.append("   */\n");
        result.append("  public double classifyInstance(Instance i) throws Exception {\n");
        result.append("    Object[] s = new Object[i.numAttributes()];\n");
        result.append("    \n");
        result.append("    for (int j = 0; j < s.length; j++) {\n");
        result.append("      if (!i.isMissing(j)) {\n");
        result.append("        if (i.attribute(j).isNominal())\n");
        result.append("          s[j] = new String(i.stringValue(j));\n");
        result.append("        else if (i.attribute(j).isNumeric())\n");
        result.append("          s[j] = new Double(i.value(j));\n");
        result.append("      }\n");
        result.append("    }\n");
        result.append("    \n");
        result.append("    // set class value to missing\n");
        result.append("    s[i.classIndex()] = null;\n");
        result.append("    \n");
        result.append("    return " + className + ".classify(s);\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Returns the revision string.\n");
        result.append("   * \n");
        result.append("   * @return        the revision\n");
        result.append("   */\n");
        result.append("  public String getRevision() {\n");
        result.append("    return RevisionUtils.extract(\"1.0\");\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Returns only the classnames and what classifier it is based on.\n");
        result.append("   *\n");
        result.append("   * @return a short description\n");
        result.append("   */\n");
        result.append("  public String toString() {\n");
        result.append("    return \"Auto-generated classifier wrapper, based on " + classifier.getClass().getName() + " (generated with Weka " + Version.VERSION + ").\\n" + "\" + this.getClass().getName() + \"/" + className + "\";\n");
        result.append("  }\n");
        result.append("\n");
        result.append("  /**\n");
        result.append("   * Runs the classfier from commandline.\n");
        result.append("   *\n");
        result.append("   * @param args the commandline arguments\n");
        result.append("   */\n");
        result.append("  public static void main(String args[]) {\n");
        result.append("    runClassifier(new WekaWrapper(), args);\n");
        result.append("  }\n");
        result.append("}\n");
        result.append("\n");
        result.append(staticClassifier);
        return result.toString();
    }
    
    public final double numInstances() {
        return this.m_WithClass;
    }
    
    public final double coverageOfTestCasesByPredictedRegions() {
        if (!this.m_CoverageStatisticsAvailable) {
            return Double.NaN;
        }
        return 100.0 * this.m_TotalCoverage / this.m_WithClass;
    }
    
    public final double sizeOfPredictedRegions() {
        if (this.m_NoPriors || !this.m_CoverageStatisticsAvailable) {
            return Double.NaN;
        }
        return 100.0 * this.m_TotalSizeOfRegions / this.m_WithClass;
    }
    
    public final double incorrect() {
        return this.m_Incorrect;
    }
    
    public final double pctIncorrect() {
        return 100.0 * this.m_Incorrect / this.m_WithClass;
    }
    
    public final double totalCost() {
        return this.m_TotalCost;
    }
    
    public final double avgCost() {
        return this.m_TotalCost / this.m_WithClass;
    }
    
    public final double correct() {
        return this.m_Correct;
    }
    
    public final double pctCorrect() {
        return 100.0 * this.m_Correct / this.m_WithClass;
    }
    
    public final double unclassified() {
        return this.m_Unclassified;
    }
    
    public final double pctUnclassified() {
        return 100.0 * this.m_Unclassified / this.m_WithClass;
    }
    
    public final double errorRate() {
        if (!this.m_ClassIsNominal) {
            return Math.sqrt(this.m_SumSqrErr / (this.m_WithClass - this.m_Unclassified));
        }
        if (this.m_CostMatrix == null) {
            return this.m_Incorrect / this.m_WithClass;
        }
        return this.avgCost();
    }
    
    public final double kappa() {
        final double[] sumRows = new double[this.m_ConfusionMatrix.length];
        final double[] sumColumns = new double[this.m_ConfusionMatrix.length];
        double sumOfWeights = 0.0;
        for (int i = 0; i < this.m_ConfusionMatrix.length; ++i) {
            for (int j = 0; j < this.m_ConfusionMatrix.length; ++j) {
                final double[] array = sumRows;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
                final double[] array2 = sumColumns;
                final int n2 = j;
                array2[n2] += this.m_ConfusionMatrix[i][j];
                sumOfWeights += this.m_ConfusionMatrix[i][j];
            }
        }
        double correct = 0.0;
        double chanceAgreement = 0.0;
        for (int k = 0; k < this.m_ConfusionMatrix.length; ++k) {
            chanceAgreement += sumRows[k] * sumColumns[k];
            correct += this.m_ConfusionMatrix[k][k];
        }
        chanceAgreement /= sumOfWeights * sumOfWeights;
        correct /= sumOfWeights;
        if (chanceAgreement < 1.0) {
            return (correct - chanceAgreement) / (1.0 - chanceAgreement);
        }
        return 1.0;
    }
    
    public final double correlationCoefficient() throws Exception {
        if (this.m_ClassIsNominal) {
            throw new Exception("Can't compute correlation coefficient: class is nominal!");
        }
        double correlation = 0.0;
        final double varActual = this.m_SumSqrClass - this.m_SumClass * this.m_SumClass / (this.m_WithClass - this.m_Unclassified);
        final double varPredicted = this.m_SumSqrPredicted - this.m_SumPredicted * this.m_SumPredicted / (this.m_WithClass - this.m_Unclassified);
        final double varProd = this.m_SumClassPredicted - this.m_SumClass * this.m_SumPredicted / (this.m_WithClass - this.m_Unclassified);
        if (varActual * varPredicted <= 0.0) {
            correlation = 0.0;
        }
        else {
            correlation = varProd / Math.sqrt(varActual * varPredicted);
        }
        return correlation;
    }
    
    public final double meanAbsoluteError() {
        return this.m_SumAbsErr / (this.m_WithClass - this.m_Unclassified);
    }
    
    public final double meanPriorAbsoluteError() {
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return this.m_SumPriorAbsErr / this.m_WithClass;
    }
    
    public final double relativeAbsoluteError() throws Exception {
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return 100.0 * this.meanAbsoluteError() / this.meanPriorAbsoluteError();
    }
    
    public final double rootMeanSquaredError() {
        return Math.sqrt(this.m_SumSqrErr / (this.m_WithClass - this.m_Unclassified));
    }
    
    public final double rootMeanPriorSquaredError() {
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return Math.sqrt(this.m_SumPriorSqrErr / this.m_WithClass);
    }
    
    public final double rootRelativeSquaredError() {
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return 100.0 * this.rootMeanSquaredError() / this.rootMeanPriorSquaredError();
    }
    
    public final double priorEntropy() throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Can't compute entropy of class prior: class numeric!");
        }
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        double entropy = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            entropy -= this.m_ClassPriors[i] / this.m_ClassPriorsSum * Utils.log2(this.m_ClassPriors[i] / this.m_ClassPriorsSum);
        }
        return entropy;
    }
    
    public final double KBInformation() throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Can't compute K&B Info score: class numeric!");
        }
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return this.m_SumKBInfo;
    }
    
    public final double KBMeanInformation() throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Can't compute K&B Info score: class numeric!");
        }
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return this.m_SumKBInfo / (this.m_WithClass - this.m_Unclassified);
    }
    
    public final double KBRelativeInformation() throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Can't compute K&B Info score: class numeric!");
        }
        if (this.m_NoPriors) {
            return Double.NaN;
        }
        return 100.0 * this.KBInformation() / this.priorEntropy();
    }
    
    public final double SFPriorEntropy() {
        if (this.m_NoPriors || !this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return this.m_SumPriorEntropy;
    }
    
    public final double SFMeanPriorEntropy() {
        if (this.m_NoPriors || !this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return this.m_SumPriorEntropy / this.m_WithClass;
    }
    
    public final double SFSchemeEntropy() {
        if (!this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return this.m_SumSchemeEntropy;
    }
    
    public final double SFMeanSchemeEntropy() {
        if (!this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return this.m_SumSchemeEntropy / (this.m_WithClass - this.m_Unclassified);
    }
    
    public final double SFEntropyGain() {
        if (this.m_NoPriors || !this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return this.m_SumPriorEntropy - this.m_SumSchemeEntropy;
    }
    
    public final double SFMeanEntropyGain() {
        if (this.m_NoPriors || !this.m_ComplexityStatisticsAvailable) {
            return Double.NaN;
        }
        return (this.m_SumPriorEntropy - this.m_SumSchemeEntropy) / (this.m_WithClass - this.m_Unclassified);
    }
    
    public String toCumulativeMarginDistributionString() throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Class must be nominal for margin distributions");
        }
        String result = "";
        double cumulativeCount = 0.0;
        for (int i = 0; i <= Evaluation.k_MarginResolution; ++i) {
            if (this.m_MarginCounts[i] != 0.0) {
                cumulativeCount += this.m_MarginCounts[i];
                final double margin = i * 2.0 / Evaluation.k_MarginResolution - 1.0;
                result = result + Utils.doubleToString(margin, 7, 3) + ' ' + Utils.doubleToString(cumulativeCount * 100.0 / this.m_WithClass, 7, 3) + '\n';
            }
            else if (i == 0) {
                result = Utils.doubleToString(-1.0, 7, 3) + ' ' + Utils.doubleToString(0.0, 7, 3) + '\n';
            }
        }
        return result;
    }
    
    @Override
    public String toSummaryString() {
        return this.toSummaryString("", false);
    }
    
    public String toSummaryString(final boolean printComplexityStatistics) {
        return this.toSummaryString("=== Summary ===\n", printComplexityStatistics);
    }
    
    public String toSummaryString(final String title, boolean printComplexityStatistics) {
        final StringBuffer text = new StringBuffer();
        if (printComplexityStatistics && this.m_NoPriors) {
            printComplexityStatistics = false;
            System.err.println("Priors disabled, cannot print complexity statistics!");
        }
        text.append(title + "\n");
        try {
            if (this.m_WithClass > 0.0) {
                if (this.m_ClassIsNominal) {
                    text.append("Correctly Classified Instances     ");
                    text.append(Utils.doubleToString(this.correct(), 12, 4) + "     " + Utils.doubleToString(this.pctCorrect(), 12, 4) + " %\n");
                    text.append("Incorrectly Classified Instances   ");
                    text.append(Utils.doubleToString(this.incorrect(), 12, 4) + "     " + Utils.doubleToString(this.pctIncorrect(), 12, 4) + " %\n");
                    text.append("Kappa statistic                    ");
                    text.append(Utils.doubleToString(this.kappa(), 12, 4) + "\n");
                    if (this.m_CostMatrix != null) {
                        text.append("Total Cost                         ");
                        text.append(Utils.doubleToString(this.totalCost(), 12, 4) + "\n");
                        text.append("Average Cost                       ");
                        text.append(Utils.doubleToString(this.avgCost(), 12, 4) + "\n");
                    }
                    if (printComplexityStatistics) {
                        text.append("K&B Relative Info Score            ");
                        text.append(Utils.doubleToString(this.KBRelativeInformation(), 12, 4) + " %\n");
                        text.append("K&B Information Score              ");
                        text.append(Utils.doubleToString(this.KBInformation(), 12, 4) + " bits");
                        text.append(Utils.doubleToString(this.KBMeanInformation(), 12, 4) + " bits/instance\n");
                    }
                }
                else {
                    text.append("Correlation coefficient            ");
                    text.append(Utils.doubleToString(this.correlationCoefficient(), 12, 4) + "\n");
                }
                if (printComplexityStatistics && this.m_ComplexityStatisticsAvailable) {
                    text.append("Class complexity | order 0         ");
                    text.append(Utils.doubleToString(this.SFPriorEntropy(), 12, 4) + " bits");
                    text.append(Utils.doubleToString(this.SFMeanPriorEntropy(), 12, 4) + " bits/instance\n");
                    text.append("Class complexity | scheme          ");
                    text.append(Utils.doubleToString(this.SFSchemeEntropy(), 12, 4) + " bits");
                    text.append(Utils.doubleToString(this.SFMeanSchemeEntropy(), 12, 4) + " bits/instance\n");
                    text.append("Complexity improvement     (Sf)    ");
                    text.append(Utils.doubleToString(this.SFEntropyGain(), 12, 4) + " bits");
                    text.append(Utils.doubleToString(this.SFMeanEntropyGain(), 12, 4) + " bits/instance\n");
                }
                text.append("Mean absolute error                ");
                text.append(Utils.doubleToString(this.meanAbsoluteError(), 12, 4) + "\n");
                text.append("Root mean squared error            ");
                text.append(Utils.doubleToString(this.rootMeanSquaredError(), 12, 4) + "\n");
                if (!this.m_NoPriors) {
                    text.append("Relative absolute error            ");
                    text.append(Utils.doubleToString(this.relativeAbsoluteError(), 12, 4) + " %\n");
                    text.append("Root relative squared error        ");
                    text.append(Utils.doubleToString(this.rootRelativeSquaredError(), 12, 4) + " %\n");
                }
                if (this.m_CoverageStatisticsAvailable) {
                    text.append("Coverage of cases (" + Utils.doubleToString(this.m_ConfLevel, 4, 2) + " level)     ");
                    text.append(Utils.doubleToString(this.coverageOfTestCasesByPredictedRegions(), 12, 4) + " %\n");
                    if (!this.m_NoPriors) {
                        text.append("Mean rel. region size (" + Utils.doubleToString(this.m_ConfLevel, 4, 2) + " level) ");
                        text.append(Utils.doubleToString(this.sizeOfPredictedRegions(), 12, 4) + " %\n");
                    }
                }
            }
            if (Utils.gr(this.unclassified(), 0.0)) {
                text.append("UnClassified Instances             ");
                text.append(Utils.doubleToString(this.unclassified(), 12, 4) + "     " + Utils.doubleToString(this.pctUnclassified(), 12, 4) + " %\n");
            }
            text.append("Total Number of Instances          ");
            text.append(Utils.doubleToString(this.m_WithClass, 12, 4) + "\n");
            if (this.m_MissingClass > 0.0) {
                text.append("Ignored Class Unknown Instances            ");
                text.append(Utils.doubleToString(this.m_MissingClass, 12, 4) + "\n");
            }
        }
        catch (Exception ex) {
            System.err.println("Arggh - Must be a bug in Evaluation class");
        }
        return text.toString();
    }
    
    public String toMatrixString() throws Exception {
        return this.toMatrixString("=== Confusion Matrix ===\n");
    }
    
    public String toMatrixString(final String title) throws Exception {
        final StringBuffer text = new StringBuffer();
        final char[] IDChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        boolean fractional = false;
        if (!this.m_ClassIsNominal) {
            throw new Exception("Evaluation: No confusion matrix possible!");
        }
        double maxval = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                double current = this.m_ConfusionMatrix[i][j];
                if (current < 0.0) {
                    current *= -10.0;
                }
                if (current > maxval) {
                    maxval = current;
                }
                final double fract = current - Math.rint(current);
                if (!fractional && Math.log(fract) / Math.log(10.0) >= -2.0) {
                    fractional = true;
                }
            }
        }
        final int IDWidth = 1 + Math.max((int)(Math.log(maxval) / Math.log(10.0) + (fractional ? 3 : 0)), (int)(Math.log(this.m_NumClasses) / Math.log(IDChars.length)));
        text.append(title).append("\n");
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (fractional) {
                text.append(" ").append(this.num2ShortID(i, IDChars, IDWidth - 3)).append("   ");
            }
            else {
                text.append(" ").append(this.num2ShortID(i, IDChars, IDWidth));
            }
        }
        text.append("   <-- classified as\n");
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                text.append(" ").append(Utils.doubleToString(this.m_ConfusionMatrix[i][j], IDWidth, fractional ? 2 : 0));
            }
            text.append(" | ").append(this.num2ShortID(i, IDChars, IDWidth)).append(" = ").append(this.m_ClassNames[i]).append("\n");
        }
        return text.toString();
    }
    
    public String toClassDetailsString() throws Exception {
        return this.toClassDetailsString("=== Detailed Accuracy By Class ===\n");
    }
    
    public String toClassDetailsString(final String title) throws Exception {
        if (!this.m_ClassIsNominal) {
            throw new Exception("Evaluation: No per class statistics possible!");
        }
        final StringBuffer text = new StringBuffer(title + "\n                 TP Rate  FP Rate" + "  Precision  Recall" + "  F-Measure  MCC    ROC Area  PRC Area  Class\n");
        for (int i = 0; i < this.m_NumClasses; ++i) {
            text.append("               " + Utils.doubleToString(this.truePositiveRate(i), 7, 3)).append("  ");
            text.append(Utils.doubleToString(this.falsePositiveRate(i), 7, 3)).append("  ");
            text.append(Utils.doubleToString(this.precision(i), 7, 3)).append("    ");
            text.append(Utils.doubleToString(this.recall(i), 7, 3)).append(" ");
            text.append(Utils.doubleToString(this.fMeasure(i), 7, 3)).append("    ");
            final double mat = this.matthewsCorrelationCoefficient(i);
            if (Utils.isMissingValue(mat)) {
                text.append("  ?  ").append("   ");
            }
            else {
                text.append(Utils.doubleToString(this.matthewsCorrelationCoefficient(i), 7, 3)).append("");
            }
            final double rocVal = this.areaUnderROC(i);
            if (Utils.isMissingValue(rocVal)) {
                text.append(" ?    ").append("   ");
            }
            else {
                text.append(Utils.doubleToString(rocVal, 7, 3)).append("   ");
            }
            final double prcVal = this.areaUnderPRC(i);
            if (Utils.isMissingValue(prcVal)) {
                text.append("  ?    ").append("     ");
            }
            else {
                text.append(Utils.doubleToString(prcVal, 7, 3)).append("     ");
            }
            text.append(this.m_ClassNames[i]).append('\n');
        }
        text.append("Weighted Avg.  " + Utils.doubleToString(this.weightedTruePositiveRate(), 7, 3));
        text.append("  " + Utils.doubleToString(this.weightedFalsePositiveRate(), 7, 3));
        text.append("  " + Utils.doubleToString(this.weightedPrecision(), 7, 3));
        text.append("    " + Utils.doubleToString(this.weightedRecall(), 7, 3));
        text.append(" " + Utils.doubleToString(this.weightedFMeasure(), 7, 3));
        text.append("    " + Utils.doubleToString(this.weightedMatthewsCorrelation(), 7, 3));
        text.append("" + Utils.doubleToString(this.weightedAreaUnderROC(), 7, 3));
        text.append("   " + Utils.doubleToString(this.weightedAreaUnderPRC(), 7, 3));
        text.append("\n");
        return text.toString();
    }
    
    public double numTruePositives(final int classIndex) {
        double correct = 0.0;
        for (int j = 0; j < this.m_NumClasses; ++j) {
            if (j == classIndex) {
                correct += this.m_ConfusionMatrix[classIndex][j];
            }
        }
        return correct;
    }
    
    public double truePositiveRate(final int classIndex) {
        double correct = 0.0;
        double total = 0.0;
        for (int j = 0; j < this.m_NumClasses; ++j) {
            if (j == classIndex) {
                correct += this.m_ConfusionMatrix[classIndex][j];
            }
            total += this.m_ConfusionMatrix[classIndex][j];
        }
        if (total == 0.0) {
            return 0.0;
        }
        return correct / total;
    }
    
    public double weightedTruePositiveRate() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double truePosTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.truePositiveRate(k);
            truePosTotal += temp * classCounts[k];
        }
        return truePosTotal / classCountSum;
    }
    
    public double numTrueNegatives(final int classIndex) {
        double correct = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i != classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j != classIndex) {
                        correct += this.m_ConfusionMatrix[i][j];
                    }
                }
            }
        }
        return correct;
    }
    
    public double trueNegativeRate(final int classIndex) {
        double correct = 0.0;
        double total = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i != classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j != classIndex) {
                        correct += this.m_ConfusionMatrix[i][j];
                    }
                    total += this.m_ConfusionMatrix[i][j];
                }
            }
        }
        if (total == 0.0) {
            return 0.0;
        }
        return correct / total;
    }
    
    public double weightedTrueNegativeRate() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double trueNegTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.trueNegativeRate(k);
            trueNegTotal += temp * classCounts[k];
        }
        return trueNegTotal / classCountSum;
    }
    
    public double numFalsePositives(final int classIndex) {
        double incorrect = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i != classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j == classIndex) {
                        incorrect += this.m_ConfusionMatrix[i][j];
                    }
                }
            }
        }
        return incorrect;
    }
    
    public double falsePositiveRate(final int classIndex) {
        double incorrect = 0.0;
        double total = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i != classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j == classIndex) {
                        incorrect += this.m_ConfusionMatrix[i][j];
                    }
                    total += this.m_ConfusionMatrix[i][j];
                }
            }
        }
        if (total == 0.0) {
            return 0.0;
        }
        return incorrect / total;
    }
    
    public double weightedFalsePositiveRate() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double falsePosTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.falsePositiveRate(k);
            falsePosTotal += temp * classCounts[k];
        }
        return falsePosTotal / classCountSum;
    }
    
    public double numFalseNegatives(final int classIndex) {
        double incorrect = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i == classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j != classIndex) {
                        incorrect += this.m_ConfusionMatrix[i][j];
                    }
                }
            }
        }
        return incorrect;
    }
    
    public double falseNegativeRate(final int classIndex) {
        double incorrect = 0.0;
        double total = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i == classIndex) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (j != classIndex) {
                        incorrect += this.m_ConfusionMatrix[i][j];
                    }
                    total += this.m_ConfusionMatrix[i][j];
                }
            }
        }
        if (total == 0.0) {
            return 0.0;
        }
        return incorrect / total;
    }
    
    public double weightedFalseNegativeRate() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double falseNegTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.falseNegativeRate(k);
            falseNegTotal += temp * classCounts[k];
        }
        return falseNegTotal / classCountSum;
    }
    
    public double matthewsCorrelationCoefficient(final int classIndex) {
        final double numTP = this.numTruePositives(classIndex);
        final double numTN = this.numTrueNegatives(classIndex);
        final double numFP = this.numFalsePositives(classIndex);
        final double numFN = this.numFalseNegatives(classIndex);
        final double n = numTP * numTN - numFP * numFN;
        double d = (numTP + numFP) * (numTP + numFN) * (numTN + numFP) * (numTN + numFN);
        d = Math.sqrt(d);
        if (d == 0.0) {
            d = 1.0;
        }
        return n / d;
    }
    
    public double weightedMatthewsCorrelation() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double mccTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.matthewsCorrelationCoefficient(k);
            if (!Utils.isMissingValue(temp)) {
                mccTotal += temp * classCounts[k];
            }
        }
        return mccTotal / classCountSum;
    }
    
    public double recall(final int classIndex) {
        return this.truePositiveRate(classIndex);
    }
    
    public double weightedRecall() {
        return this.weightedTruePositiveRate();
    }
    
    public double precision(final int classIndex) {
        double correct = 0.0;
        double total = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i == classIndex) {
                correct += this.m_ConfusionMatrix[i][classIndex];
            }
            total += this.m_ConfusionMatrix[i][classIndex];
        }
        if (total == 0.0) {
            return 0.0;
        }
        return correct / total;
    }
    
    public double weightedPrecision() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double precisionTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.precision(k);
            precisionTotal += temp * classCounts[k];
        }
        return precisionTotal / classCountSum;
    }
    
    public double fMeasure(final int classIndex) {
        final double precision = this.precision(classIndex);
        final double recall = this.recall(classIndex);
        if (precision + recall == 0.0) {
            return 0.0;
        }
        return 2.0 * precision * recall / (precision + recall);
    }
    
    public double weightedFMeasure() {
        final double[] classCounts = new double[this.m_NumClasses];
        double classCountSum = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            for (int j = 0; j < this.m_NumClasses; ++j) {
                final double[] array = classCounts;
                final int n = i;
                array[n] += this.m_ConfusionMatrix[i][j];
            }
            classCountSum += classCounts[i];
        }
        double fMeasureTotal = 0.0;
        for (int k = 0; k < this.m_NumClasses; ++k) {
            final double temp = this.fMeasure(k);
            fMeasureTotal += temp * classCounts[k];
        }
        return fMeasureTotal / classCountSum;
    }
    
    public double unweightedMacroFmeasure() {
        final Stats rr = new Stats();
        for (int c = 0; c < this.m_NumClasses; ++c) {
            if (this.numTruePositives(c) + this.numFalseNegatives(c) > 0.0) {
                rr.add(this.fMeasure(c));
            }
        }
        rr.calculateDerived();
        return rr.mean;
    }
    
    public double unweightedMicroFmeasure() {
        double tp = 0.0;
        double fn = 0.0;
        double fp = 0.0;
        for (int c = 0; c < this.m_NumClasses; ++c) {
            tp += this.numTruePositives(c);
            fn += this.numFalseNegatives(c);
            fp += this.numFalsePositives(c);
        }
        return 2.0 * tp / (2.0 * tp + fn + fp);
    }
    
    public void setPriors(final Instances train) throws Exception {
        this.m_NoPriors = false;
        if (!this.m_ClassIsNominal) {
            this.m_NumTrainClassVals = 0;
            this.m_TrainClassVals = null;
            this.m_TrainClassWeights = null;
            this.m_PriorEstimator = null;
            this.m_MinTarget = Double.MAX_VALUE;
            this.m_MaxTarget = -1.7976931348623157E308;
            for (int i = 0; i < train.numInstances(); ++i) {
                final Instance currentInst = train.instance(i);
                if (!currentInst.classIsMissing()) {
                    this.addNumericTrainClass(currentInst.classValue(), currentInst.weight());
                }
            }
            this.m_ClassPriors[0] = (this.m_ClassPriorsSum = 0.0);
            for (int i = 0; i < train.numInstances(); ++i) {
                if (!train.instance(i).classIsMissing()) {
                    final double[] classPriors = this.m_ClassPriors;
                    final int n = 0;
                    classPriors[n] += train.instance(i).classValue() * train.instance(i).weight();
                    this.m_ClassPriorsSum += train.instance(i).weight();
                }
            }
        }
        else {
            for (int i = 0; i < this.m_NumClasses; ++i) {
                this.m_ClassPriors[i] = 1.0;
            }
            this.m_ClassPriorsSum = this.m_NumClasses;
            for (int i = 0; i < train.numInstances(); ++i) {
                if (!train.instance(i).classIsMissing()) {
                    final double[] classPriors2 = this.m_ClassPriors;
                    final int n2 = (int)train.instance(i).classValue();
                    classPriors2[n2] += train.instance(i).weight();
                    this.m_ClassPriorsSum += train.instance(i).weight();
                }
            }
            this.m_MaxTarget = this.m_NumClasses;
            this.m_MinTarget = 0.0;
        }
    }
    
    public double[] getClassPriors() {
        return this.m_ClassPriors;
    }
    
    public void updatePriors(final Instance instance) throws Exception {
        if (!instance.classIsMissing()) {
            if (!this.m_ClassIsNominal) {
                this.addNumericTrainClass(instance.classValue(), instance.weight());
                final double[] classPriors = this.m_ClassPriors;
                final int n = 0;
                classPriors[n] += instance.classValue() * instance.weight();
                this.m_ClassPriorsSum += instance.weight();
            }
            else {
                final double[] classPriors2 = this.m_ClassPriors;
                final int n2 = (int)instance.classValue();
                classPriors2[n2] += instance.weight();
                this.m_ClassPriorsSum += instance.weight();
            }
        }
    }
    
    public void useNoPriors() {
        this.m_NoPriors = true;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        final Evaluation cmp = (Evaluation)obj;
        if (this.m_ClassIsNominal != cmp.m_ClassIsNominal) {
            return false;
        }
        if (this.m_NumClasses != cmp.m_NumClasses) {
            return false;
        }
        if (this.m_Incorrect != cmp.m_Incorrect) {
            return false;
        }
        if (this.m_Correct != cmp.m_Correct) {
            return false;
        }
        if (this.m_Unclassified != cmp.m_Unclassified) {
            return false;
        }
        if (this.m_MissingClass != cmp.m_MissingClass) {
            return false;
        }
        if (this.m_WithClass != cmp.m_WithClass) {
            return false;
        }
        if (this.m_SumErr != cmp.m_SumErr) {
            return false;
        }
        if (this.m_SumAbsErr != cmp.m_SumAbsErr) {
            return false;
        }
        if (this.m_SumSqrErr != cmp.m_SumSqrErr) {
            return false;
        }
        if (this.m_SumClass != cmp.m_SumClass) {
            return false;
        }
        if (this.m_SumSqrClass != cmp.m_SumSqrClass) {
            return false;
        }
        if (this.m_SumPredicted != cmp.m_SumPredicted) {
            return false;
        }
        if (this.m_SumSqrPredicted != cmp.m_SumSqrPredicted) {
            return false;
        }
        if (this.m_SumClassPredicted != cmp.m_SumClassPredicted) {
            return false;
        }
        if (this.m_ClassIsNominal) {
            for (int i = 0; i < this.m_NumClasses; ++i) {
                for (int j = 0; j < this.m_NumClasses; ++j) {
                    if (this.m_ConfusionMatrix[i][j] != cmp.m_ConfusionMatrix[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    protected static String makeOptionString(final Classifier classifier, final boolean globalInfo) {
        final StringBuffer optionsText = new StringBuffer("");
        optionsText.append("\n\nGeneral options:\n\n");
        optionsText.append("-h or -help\n");
        optionsText.append("\tOutput help information.\n");
        optionsText.append("-synopsis or -info\n");
        optionsText.append("\tOutput synopsis for classifier (use in conjunction  with -h)\n");
        optionsText.append("-t <name of training file>\n");
        optionsText.append("\tSets training file.\n");
        optionsText.append("-T <name of test file>\n");
        optionsText.append("\tSets test file. If missing, a cross-validation will be performed\n");
        optionsText.append("\ton the training data.\n");
        optionsText.append("-c <class index>\n");
        optionsText.append("\tSets index of class attribute (default: last).\n");
        optionsText.append("-x <number of folds>\n");
        optionsText.append("\tSets number of folds for cross-validation (default: 10).\n");
        optionsText.append("-no-cv\n");
        optionsText.append("\tDo not perform any cross validation.\n");
        optionsText.append("-split-percentage <percentage>\n");
        optionsText.append("\tSets the percentage for the train/test set split, e.g., 66.\n");
        optionsText.append("-preserve-order\n");
        optionsText.append("\tPreserves the order in the percentage split.\n");
        optionsText.append("-s <random number seed>\n");
        optionsText.append("\tSets random number seed for cross-validation or percentage split\n");
        optionsText.append("\t(default: 1).\n");
        optionsText.append("-m <name of file with cost matrix>\n");
        optionsText.append("\tSets file with cost matrix.\n");
        optionsText.append("-l <name of input file>\n");
        optionsText.append("\tSets model input file. In case the filename ends with '.xml',\n");
        optionsText.append("\ta PMML file is loaded or, if that fails, options are loaded\n");
        optionsText.append("\tfrom the XML file.\n");
        optionsText.append("-d <name of output file>\n");
        optionsText.append("\tSets model output file. In case the filename ends with '.xml',\n");
        optionsText.append("\tonly the options are saved to the XML file, not the model.\n");
        optionsText.append("-v\n");
        optionsText.append("\tOutputs no statistics for training data.\n");
        optionsText.append("-o\n");
        optionsText.append("\tOutputs statistics only, not the classifier.\n");
        optionsText.append("-i\n");
        optionsText.append("\tOutputs detailed information-retrieval");
        optionsText.append(" statistics for each class.\n");
        optionsText.append("-k\n");
        optionsText.append("\tOutputs information-theoretic statistics.\n");
        optionsText.append("-classifications \"weka.classifiers.evaluation.output.prediction.AbstractOutput + options\"\n");
        optionsText.append("\tUses the specified class for generating the classification output.\n");
        optionsText.append("\tE.g.: " + PlainText.class.getName() + "\n");
        optionsText.append("-p range\n");
        optionsText.append("\tOutputs predictions for test instances (or the train instances if\n");
        optionsText.append("\tno test instances provided and -no-cv is used), along with the \n");
        optionsText.append("\tattributes in the specified range (and nothing else). \n");
        optionsText.append("\tUse '-p 0' if no attributes are desired.\n");
        optionsText.append("\tDeprecated: use \"-classifications ...\" instead.\n");
        optionsText.append("-distribution\n");
        optionsText.append("\tOutputs the distribution instead of only the prediction\n");
        optionsText.append("\tin conjunction with the '-p' option (only nominal classes).\n");
        optionsText.append("\tDeprecated: use \"-classifications ...\" instead.\n");
        optionsText.append("-r\n");
        optionsText.append("\tOnly outputs cumulative margin distribution.\n");
        if (classifier instanceof Sourcable) {
            optionsText.append("-z <class name>\n");
            optionsText.append("\tOnly outputs the source representation of the classifier,\n\tgiving it the supplied name.\n");
        }
        if (classifier instanceof Drawable) {
            optionsText.append("-g\n");
            optionsText.append("\tOnly outputs the graph representation of the classifier.\n");
        }
        optionsText.append("-xml filename | xml-string\n");
        optionsText.append("\tRetrieves the options from the XML-data instead of the command line.\n");
        optionsText.append("-threshold-file <file>\n");
        optionsText.append("\tThe file to save the threshold data to.\n\tThe format is determined by the extensions, e.g., '.arff' for ARFF \n\tformat or '.csv' for CSV.\n");
        optionsText.append("-threshold-label <label>\n");
        optionsText.append("\tThe class label to determine the threshold data for\n\t(default is the first label)\n");
        if (classifier instanceof OptionHandler) {
            optionsText.append("\nOptions specific to " + classifier.getClass().getName() + ":\n\n");
            final Enumeration enu = ((OptionHandler)classifier).listOptions();
            while (enu.hasMoreElements()) {
                final Option option = enu.nextElement();
                optionsText.append(option.synopsis() + '\n');
                optionsText.append(option.description() + "\n");
            }
        }
        if (globalInfo) {
            try {
                final String gi = getGlobalInfo(classifier);
                optionsText.append(gi);
            }
            catch (Exception ex) {}
        }
        return optionsText.toString();
    }
    
    protected static String getGlobalInfo(final Classifier classifier) throws Exception {
        final BeanInfo bi = Introspector.getBeanInfo(classifier.getClass());
        final MethodDescriptor[] methods = bi.getMethodDescriptors();
        final Object[] args = new Object[0];
        String result = "\nSynopsis for " + classifier.getClass().getName() + ":\n\n";
        for (int i = 0; i < methods.length; ++i) {
            final String name = methods[i].getDisplayName();
            final Method meth = methods[i].getMethod();
            if (name.equals("globalInfo")) {
                final String globalInfo = (String)meth.invoke(classifier, args);
                result += globalInfo;
                break;
            }
        }
        return result;
    }
    
    protected String num2ShortID(int num, final char[] IDChars, final int IDWidth) {
        final char[] ID = new char[IDWidth];
        int i;
        for (i = IDWidth - 1; i >= 0; --i) {
            ID[i] = IDChars[num % IDChars.length];
            num = num / IDChars.length - 1;
            if (num < 0) {
                break;
            }
        }
        --i;
        while (i >= 0) {
            ID[i] = ' ';
            --i;
        }
        return new String(ID);
    }
    
    protected double[] makeDistribution(final double predictedClass) {
        final double[] result = new double[this.m_NumClasses];
        if (Utils.isMissingValue(predictedClass)) {
            return result;
        }
        if (this.m_ClassIsNominal) {
            result[(int)predictedClass] = 1.0;
        }
        else {
            result[0] = predictedClass;
        }
        return result;
    }
    
    protected void updateStatsForClassifier(final double[] predictedDistribution, final Instance instance) throws Exception {
        final int actualClass = (int)instance.classValue();
        if (!instance.classIsMissing()) {
            this.updateMargins(predictedDistribution, actualClass, instance.weight());
            int predictedClass = -1;
            double bestProb = 0.0;
            for (int i = 0; i < this.m_NumClasses; ++i) {
                if (predictedDistribution[i] > bestProb) {
                    predictedClass = i;
                    bestProb = predictedDistribution[i];
                }
            }
            this.m_WithClass += instance.weight();
            if (this.m_CostMatrix != null) {
                if (predictedClass < 0) {
                    this.m_TotalCost += instance.weight() * this.m_CostMatrix.getMaxCost(actualClass, instance);
                }
                else {
                    this.m_TotalCost += instance.weight() * this.m_CostMatrix.getElement(actualClass, predictedClass, instance);
                }
            }
            if (predictedClass < 0) {
                this.m_Unclassified += instance.weight();
                return;
            }
            final double predictedProb = Math.max(Double.MIN_VALUE, predictedDistribution[actualClass]);
            final double priorProb = Math.max(Double.MIN_VALUE, this.m_ClassPriors[actualClass] / this.m_ClassPriorsSum);
            if (predictedProb >= priorProb) {
                this.m_SumKBInfo += (Utils.log2(predictedProb) - Utils.log2(priorProb)) * instance.weight();
            }
            else {
                this.m_SumKBInfo -= (Utils.log2(1.0 - predictedProb) - Utils.log2(1.0 - priorProb)) * instance.weight();
            }
            this.m_SumSchemeEntropy -= Utils.log2(predictedProb) * instance.weight();
            this.m_SumPriorEntropy -= Utils.log2(priorProb) * instance.weight();
            this.updateNumericScores(predictedDistribution, this.makeDistribution(instance.classValue()), instance.weight());
            final int[] indices = Utils.sort(predictedDistribution);
            double sum = 0.0;
            double sizeOfRegions = 0.0;
            for (int j = predictedDistribution.length - 1; j >= 0 && sum < this.m_ConfLevel; --j) {
                sum += predictedDistribution[indices[j]];
                ++sizeOfRegions;
                if (actualClass == indices[j]) {
                    this.m_TotalCoverage += instance.weight();
                }
            }
            this.m_TotalSizeOfRegions += sizeOfRegions / (this.m_MaxTarget - this.m_MinTarget);
            final double[] array = this.m_ConfusionMatrix[actualClass];
            final int n = predictedClass;
            array[n] += instance.weight();
            if (predictedClass != actualClass) {
                this.m_Incorrect += instance.weight();
            }
            else {
                this.m_Correct += instance.weight();
            }
        }
        else {
            this.m_MissingClass += instance.weight();
        }
    }
    
    protected void updateStatsForIntervalEstimator(final IntervalEstimator classifier, final Instance classMissing, final double classValue) throws Exception {
        final double[][] preds = classifier.predictIntervals(classMissing, this.m_ConfLevel);
        if (this.m_Predictions != null) {
            this.m_Predictions.lastElement().setPredictionIntervals(preds);
        }
        for (int i = 0; i < preds.length; ++i) {
            this.m_TotalSizeOfRegions += (preds[i][1] - preds[i][0]) / (this.m_MaxTarget - this.m_MinTarget);
        }
        for (int i = 0; i < preds.length; ++i) {
            if (preds[i][1] >= classValue && preds[i][0] <= classValue) {
                this.m_TotalCoverage += classMissing.weight();
                break;
            }
        }
    }
    
    protected void updateStatsForConditionalDensityEstimator(final ConditionalDensityEstimator classifier, final Instance classMissing, final double classValue) throws Exception {
        if (this.m_PriorEstimator == null) {
            this.setNumericPriorsFromBuffer();
        }
        this.m_SumSchemeEntropy -= classifier.logDensity(classMissing, classValue) * classMissing.weight() / Utils.log2;
        this.m_SumPriorEntropy -= this.m_PriorEstimator.logDensity(classValue) * classMissing.weight() / Utils.log2;
    }
    
    protected void updateStatsForPredictor(final double predictedValue, final Instance instance) throws Exception {
        if (!instance.classIsMissing()) {
            this.m_WithClass += instance.weight();
            if (Utils.isMissingValue(predictedValue)) {
                this.m_Unclassified += instance.weight();
                return;
            }
            this.m_SumClass += instance.weight() * instance.classValue();
            this.m_SumSqrClass += instance.weight() * instance.classValue() * instance.classValue();
            this.m_SumClassPredicted += instance.weight() * instance.classValue() * predictedValue;
            this.m_SumPredicted += instance.weight() * predictedValue;
            this.m_SumSqrPredicted += instance.weight() * predictedValue * predictedValue;
            this.updateNumericScores(this.makeDistribution(predictedValue), this.makeDistribution(instance.classValue()), instance.weight());
        }
        else {
            this.m_MissingClass += instance.weight();
        }
    }
    
    protected void updateMargins(final double[] predictedDistribution, final int actualClass, final double weight) {
        final double probActual = predictedDistribution[actualClass];
        double probNext = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            if (i != actualClass && predictedDistribution[i] > probNext) {
                probNext = predictedDistribution[i];
            }
        }
        final double margin = probActual - probNext;
        final int bin = (int)((margin + 1.0) / 2.0 * Evaluation.k_MarginResolution);
        final double[] marginCounts = this.m_MarginCounts;
        final int n = bin;
        marginCounts[n] += weight;
    }
    
    protected void updateNumericScores(final double[] predicted, final double[] actual, final double weight) {
        double sumErr = 0.0;
        double sumAbsErr = 0.0;
        double sumSqrErr = 0.0;
        double sumPriorAbsErr = 0.0;
        double sumPriorSqrErr = 0.0;
        for (int i = 0; i < this.m_NumClasses; ++i) {
            double diff = predicted[i] - actual[i];
            sumErr += diff;
            sumAbsErr += Math.abs(diff);
            sumSqrErr += diff * diff;
            diff = this.m_ClassPriors[i] / this.m_ClassPriorsSum - actual[i];
            sumPriorAbsErr += Math.abs(diff);
            sumPriorSqrErr += diff * diff;
        }
        this.m_SumErr += weight * sumErr / this.m_NumClasses;
        this.m_SumAbsErr += weight * sumAbsErr / this.m_NumClasses;
        this.m_SumSqrErr += weight * sumSqrErr / this.m_NumClasses;
        this.m_SumPriorAbsErr += weight * sumPriorAbsErr / this.m_NumClasses;
        this.m_SumPriorSqrErr += weight * sumPriorSqrErr / this.m_NumClasses;
    }
    
    protected void addNumericTrainClass(final double classValue, final double weight) {
        if (classValue > this.m_MaxTarget) {
            this.m_MaxTarget = classValue;
        }
        if (classValue < this.m_MinTarget) {
            this.m_MinTarget = classValue;
        }
        if (this.m_TrainClassVals == null) {
            this.m_TrainClassVals = new double[100];
            this.m_TrainClassWeights = new double[100];
        }
        if (this.m_NumTrainClassVals == this.m_TrainClassVals.length) {
            double[] temp = new double[this.m_TrainClassVals.length * 2];
            System.arraycopy(this.m_TrainClassVals, 0, temp, 0, this.m_TrainClassVals.length);
            this.m_TrainClassVals = temp;
            temp = new double[this.m_TrainClassWeights.length * 2];
            System.arraycopy(this.m_TrainClassWeights, 0, temp, 0, this.m_TrainClassWeights.length);
            this.m_TrainClassWeights = temp;
        }
        this.m_TrainClassVals[this.m_NumTrainClassVals] = classValue;
        this.m_TrainClassWeights[this.m_NumTrainClassVals] = weight;
        ++this.m_NumTrainClassVals;
    }
    
    protected void setNumericPriorsFromBuffer() {
        this.m_PriorEstimator = new UnivariateKernelEstimator();
        for (int i = 0; i < this.m_NumTrainClassVals; ++i) {
            this.m_PriorEstimator.addValue(this.m_TrainClassVals[i], this.m_TrainClassWeights[i]);
        }
    }
    
    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 9101 $");
    }
    
    static {
        Evaluation.k_MarginResolution = 500;
    }
}
