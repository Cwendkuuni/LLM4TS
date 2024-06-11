package weka.core;

import weka.gui.*;
import java.io.*;
import java.util.*;

public class FindWithCapabilities implements OptionHandler, CapabilitiesHandler, RevisionHandler
{
    protected Capabilities m_Capabilities;
    protected Capabilities m_NotCapabilities;
    protected Vector<String> m_Packages;
    protected CapabilitiesHandler m_Handler;
    protected String m_Filename;
    protected SingleIndex m_ClassIndex;
    protected String m_Superclass;
    protected boolean m_GenericPropertiesCreator;
    protected Vector<String> m_Matches;
    protected Vector<String> m_Misses;
    
    public FindWithCapabilities() {
        this.m_Capabilities = new Capabilities(this);
        this.m_NotCapabilities = new Capabilities(this);
        this.m_Packages = new Vector<String>();
        this.m_Handler = null;
        this.m_Filename = "";
        this.m_ClassIndex = new SingleIndex();
        this.m_Superclass = "";
        this.m_GenericPropertiesCreator = false;
        this.m_Matches = new Vector<String>();
        this.m_Misses = new Vector<String>();
    }
    
    @Override
    public Enumeration listOptions() {
        final Vector<Option> result = new Vector<Option>();
        result.addElement(new Option("", "", 0, "All class and attribute options can be prefixed with 'not',\ne.g., '-not-numeric-class'. This makes sure that the returned\nschemes 'cannot' handle numeric classes."));
        result.addElement(new Option("\tThe minimum number of instances (default 1).", "num-instances", 1, "-num-instances <num>"));
        result.addElement(new Option("\tMust handle unray classes.", "unary-class", 0, "-unary-class"));
        result.addElement(new Option("\tMust handle binary classes.", "binary-class", 0, "-binary-class"));
        result.addElement(new Option("\tMust handle nominal classes.", "nominal-class", 0, "-nominal-class"));
        result.addElement(new Option("\tMust handle numeric classes.", "numeric-class", 0, "-numeric-class"));
        result.addElement(new Option("\tMust handle string classes.", "string-class", 0, "-string-class"));
        result.addElement(new Option("\tMust handle date classes.", "date-class", 0, "-date-class"));
        result.addElement(new Option("\tMust handle relational classes.", "relational-class", 0, "-relational-class"));
        result.addElement(new Option("\tMust handle missing class values.", "missing-class-values", 0, "-missing-class-values"));
        result.addElement(new Option("\tDoesn't need a class.", "no-class", 0, "-no-class"));
        result.addElement(new Option("\tMust handle unary attributes.", "unary-atts", 0, "-unary-atts"));
        result.addElement(new Option("\tMust handle binary attributes.", "binary-atts", 0, "-binary-atts"));
        result.addElement(new Option("\tMust handle nominal attributes.", "nominal-atts", 0, "-nominal-atts"));
        result.addElement(new Option("\tMust handle numeric attributes.", "numeric-atts", 0, "-numeric-atts"));
        result.addElement(new Option("\tMust handle string attributes.", "string-atts", 0, "-string-atts"));
        result.addElement(new Option("\tMust handle date attributes.", "date-atts", 0, "-date-atts"));
        result.addElement(new Option("\tMust handle relational attributes.", "relational-atts", 0, "-relational-atts"));
        result.addElement(new Option("\tMust handle missing attribute values.", "missing-att-values", 0, "-missing-att-values"));
        result.addElement(new Option("\tMust handle multi-instance data.", "only-multiinstance", 0, "-only-multiinstance"));
        result.addElement(new Option("\tThe Capabilities handler to base the handling on.\n\tThe other parameters can be used to override the ones\n\tdetermined from the handler. Additional parameters for\n\thandler can be passed on after the '--'.\n\tEither '-W' or '-t' can be used.", "W", 1, "-W <classname>"));
        result.addElement(new Option("\tThe dataset to base the capabilities on.\n\tThe other parameters can be used to override the ones\n\tdetermined from the handler.\n\tEither '-t' or '-W' can be used.", "t", 1, "-t <file>"));
        result.addElement(new Option("\tThe index of the class attribute, -1 for none.\n\t'first' and 'last' are also valid.\n\tOnly in conjunction with option '-t'.", "c", 1, "-c <num>"));
        result.addElement(new Option("\tSuperclass to look for in the packages.\n", "superclass", 1, "-superclass"));
        result.addElement(new Option("\tComma-separated list of packages to search in.", "packages", 1, "-packages"));
        result.addElement(new Option("\tRetrieves the package list from the GenericPropertiesCreator\n\tfor the given superclass. (overrides -packages <list>).", "generic", 1, "-generic"));
        result.addElement(new Option("\tAlso prints the classname that didn't match the criteria.", "misses", 0, "-misses"));
        return result.elements();
    }
    
    @Override
    public void setOptions(final String[] options) throws Exception {
        this.m_Capabilities = new Capabilities(this);
        boolean initialized = false;
        String tmpStr = Utils.getOption('W', options);
        if (tmpStr.length() != 0) {
            final Class cls = Class.forName(tmpStr);
            if (!ClassDiscovery.hasInterface(CapabilitiesHandler.class, cls)) {
                throw new IllegalArgumentException("Class '" + tmpStr + "' is not a CapabilitiesHandler!");
            }
            initialized = true;
            final CapabilitiesHandler handler = cls.newInstance();
            if (handler instanceof OptionHandler) {
                ((OptionHandler)handler).setOptions(Utils.partitionOptions(options));
            }
            this.setHandler(handler);
        }
        else {
            tmpStr = Utils.getOption('c', options);
            if (tmpStr.length() != 0) {
                this.setClassIndex(tmpStr);
            }
            else {
                this.setClassIndex("last");
            }
            tmpStr = Utils.getOption('t', options);
            this.setFilename(tmpStr);
        }
        tmpStr = Utils.getOption("num-instances", options);
        if (tmpStr.length() != 0) {
            this.m_Capabilities.setMinimumNumberInstances(Integer.parseInt(tmpStr));
        }
        else if (!initialized) {
            this.m_Capabilities.setMinimumNumberInstances(1);
        }
        if (Utils.getFlag("no-class", options)) {
            this.enable(Capabilities.Capability.NO_CLASS);
        }
        if (Utils.getFlag("not-no-class", options)) {
            this.enableNot(Capabilities.Capability.NO_CLASS);
        }
        if (!this.m_Capabilities.handles(Capabilities.Capability.NO_CLASS)) {
            if (Utils.getFlag("nominal-class", options)) {
                this.enable(Capabilities.Capability.NOMINAL_CLASS);
                this.disable(Capabilities.Capability.BINARY_CLASS);
            }
            if (Utils.getFlag("binary-class", options)) {
                this.enable(Capabilities.Capability.BINARY_CLASS);
                this.disable(Capabilities.Capability.UNARY_CLASS);
            }
            if (Utils.getFlag("unary-class", options)) {
                this.enable(Capabilities.Capability.UNARY_CLASS);
            }
            if (Utils.getFlag("numeric-class", options)) {
                this.enable(Capabilities.Capability.NUMERIC_CLASS);
            }
            if (Utils.getFlag("string-class", options)) {
                this.enable(Capabilities.Capability.STRING_CLASS);
            }
            if (Utils.getFlag("date-class", options)) {
                this.enable(Capabilities.Capability.DATE_CLASS);
            }
            if (Utils.getFlag("relational-class", options)) {
                this.enable(Capabilities.Capability.RELATIONAL_CLASS);
            }
            if (Utils.getFlag("missing-class-values", options)) {
                this.enable(Capabilities.Capability.MISSING_CLASS_VALUES);
            }
        }
        if (Utils.getFlag("not-nominal-class", options)) {
            this.enableNot(Capabilities.Capability.NOMINAL_CLASS);
            this.disableNot(Capabilities.Capability.BINARY_CLASS);
        }
        if (Utils.getFlag("not-binary-class", options)) {
            this.enableNot(Capabilities.Capability.BINARY_CLASS);
            this.disableNot(Capabilities.Capability.UNARY_CLASS);
        }
        if (Utils.getFlag("not-unary-class", options)) {
            this.enableNot(Capabilities.Capability.UNARY_CLASS);
        }
        if (Utils.getFlag("not-numeric-class", options)) {
            this.enableNot(Capabilities.Capability.NUMERIC_CLASS);
        }
        if (Utils.getFlag("not-string-class", options)) {
            this.enableNot(Capabilities.Capability.STRING_CLASS);
        }
        if (Utils.getFlag("not-date-class", options)) {
            this.enableNot(Capabilities.Capability.DATE_CLASS);
        }
        if (Utils.getFlag("not-relational-class", options)) {
            this.enableNot(Capabilities.Capability.RELATIONAL_CLASS);
        }
        if (Utils.getFlag("not-relational-class", options)) {
            this.enableNot(Capabilities.Capability.RELATIONAL_CLASS);
        }
        if (Utils.getFlag("not-missing-class-values", options)) {
            this.enableNot(Capabilities.Capability.MISSING_CLASS_VALUES);
        }
        if (Utils.getFlag("nominal-atts", options)) {
            this.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
            this.disable(Capabilities.Capability.BINARY_ATTRIBUTES);
        }
        if (Utils.getFlag("binary-atts", options)) {
            this.enable(Capabilities.Capability.BINARY_ATTRIBUTES);
            this.disable(Capabilities.Capability.UNARY_ATTRIBUTES);
        }
        if (Utils.getFlag("unary-atts", options)) {
            this.enable(Capabilities.Capability.UNARY_ATTRIBUTES);
        }
        if (Utils.getFlag("numeric-atts", options)) {
            this.enable(Capabilities.Capability.NUMERIC_ATTRIBUTES);
        }
        if (Utils.getFlag("string-atts", options)) {
            this.enable(Capabilities.Capability.STRING_ATTRIBUTES);
        }
        if (Utils.getFlag("date-atts", options)) {
            this.enable(Capabilities.Capability.DATE_ATTRIBUTES);
        }
        if (Utils.getFlag("relational-atts", options)) {
            this.enable(Capabilities.Capability.RELATIONAL_ATTRIBUTES);
        }
        if (Utils.getFlag("missing-att-values", options)) {
            this.enable(Capabilities.Capability.MISSING_VALUES);
        }
        if (Utils.getFlag("not-nominal-atts", options)) {
            this.enableNot(Capabilities.Capability.NOMINAL_ATTRIBUTES);
            this.disableNot(Capabilities.Capability.BINARY_ATTRIBUTES);
        }
        if (Utils.getFlag("not-binary-atts", options)) {
            this.enableNot(Capabilities.Capability.BINARY_ATTRIBUTES);
            this.disableNot(Capabilities.Capability.UNARY_ATTRIBUTES);
        }
        if (Utils.getFlag("not-unary-atts", options)) {
            this.enableNot(Capabilities.Capability.UNARY_ATTRIBUTES);
        }
        if (Utils.getFlag("not-numeric-atts", options)) {
            this.enableNot(Capabilities.Capability.NUMERIC_ATTRIBUTES);
        }
        if (Utils.getFlag("not-string-atts", options)) {
            this.enableNot(Capabilities.Capability.STRING_ATTRIBUTES);
        }
        if (Utils.getFlag("not-date-atts", options)) {
            this.enableNot(Capabilities.Capability.DATE_ATTRIBUTES);
        }
        if (Utils.getFlag("not-relational-atts", options)) {
            this.enableNot(Capabilities.Capability.RELATIONAL_ATTRIBUTES);
        }
        if (Utils.getFlag("not-missing-att-values", options)) {
            this.enableNot(Capabilities.Capability.MISSING_VALUES);
        }
        if (Utils.getFlag("only-multiinstance", options)) {
            this.enable(Capabilities.Capability.ONLY_MULTIINSTANCE);
        }
        tmpStr = Utils.getOption("superclass", options);
        if (tmpStr.length() != 0) {
            this.m_Superclass = tmpStr;
            tmpStr = Utils.getOption("packages", options);
            if (tmpStr.length() != 0) {
                final StringTokenizer tok = new StringTokenizer(tmpStr, ",");
                this.m_Packages = new Vector<String>();
                while (tok.hasMoreTokens()) {
                    this.m_Packages.add(tok.nextToken());
                }
            }
            if (Utils.getFlag("generic", options)) {
                final GenericPropertiesCreator creator = new GenericPropertiesCreator();
                creator.execute(false);
                final Properties props = creator.getInputProperties();
                final StringTokenizer tok = new StringTokenizer(props.getProperty(this.m_Superclass), ",");
                this.m_Packages = new Vector<String>();
                while (tok.hasMoreTokens()) {
                    this.m_Packages.add(tok.nextToken());
                }
            }
            return;
        }
        throw new IllegalArgumentException("A superclass has to be specified!");
    }
    
    @Override
    public String[] getOptions() {
        final Vector<String> result = new Vector<String>();
        result.add("-num-instances");
        result.add("" + this.m_Capabilities.getMinimumNumberInstances());
        if (this.isEnabled(Capabilities.Capability.NO_CLASS)) {
            result.add("-no-class");
        }
        else {
            if (this.isEnabled(Capabilities.Capability.UNARY_CLASS)) {
                result.add("-unary-class");
            }
            if (this.isEnabled(Capabilities.Capability.BINARY_CLASS)) {
                result.add("-binary-class");
            }
            if (this.isEnabled(Capabilities.Capability.NOMINAL_CLASS)) {
                result.add("-nominal-class");
            }
            if (this.isEnabled(Capabilities.Capability.NUMERIC_CLASS)) {
                result.add("-numeric-class");
            }
            if (this.isEnabled(Capabilities.Capability.STRING_CLASS)) {
                result.add("-string-class");
            }
            if (this.isEnabled(Capabilities.Capability.DATE_CLASS)) {
                result.add("-date-class");
            }
            if (this.isEnabled(Capabilities.Capability.RELATIONAL_CLASS)) {
                result.add("-relational-class");
            }
            if (this.isEnabled(Capabilities.Capability.MISSING_CLASS_VALUES)) {
                result.add("-missing-class-values");
            }
        }
        if (this.isEnabled(Capabilities.Capability.UNARY_ATTRIBUTES)) {
            result.add("-unary-atts");
        }
        if (this.isEnabled(Capabilities.Capability.BINARY_ATTRIBUTES)) {
            result.add("-binary-atts");
        }
        if (this.isEnabled(Capabilities.Capability.NOMINAL_ATTRIBUTES)) {
            result.add("-nominal-atts");
        }
        if (this.isEnabled(Capabilities.Capability.NUMERIC_ATTRIBUTES)) {
            result.add("-numeric-atts");
        }
        if (this.isEnabled(Capabilities.Capability.STRING_ATTRIBUTES)) {
            result.add("-string-atts");
        }
        if (this.isEnabled(Capabilities.Capability.DATE_ATTRIBUTES)) {
            result.add("-date-atts");
        }
        if (this.isEnabled(Capabilities.Capability.RELATIONAL_ATTRIBUTES)) {
            result.add("-relational-atts");
        }
        if (this.isEnabled(Capabilities.Capability.MISSING_VALUES)) {
            result.add("-missing-att-values");
        }
        if (this.isEnabledNot(Capabilities.Capability.NO_CLASS)) {
            result.add("-not-no-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.UNARY_CLASS)) {
            result.add("-not-unary-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.BINARY_CLASS)) {
            result.add("-not-binary-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.NOMINAL_CLASS)) {
            result.add("-not-nominal-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.NUMERIC_CLASS)) {
            result.add("-not-numeric-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.STRING_CLASS)) {
            result.add("-not-string-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.DATE_CLASS)) {
            result.add("-not-date-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.RELATIONAL_CLASS)) {
            result.add("-not-relational-class");
        }
        if (this.isEnabledNot(Capabilities.Capability.MISSING_CLASS_VALUES)) {
            result.add("-not-missing-class-values");
        }
        if (this.isEnabledNot(Capabilities.Capability.UNARY_ATTRIBUTES)) {
            result.add("-not-unary-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.BINARY_ATTRIBUTES)) {
            result.add("-not-binary-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.NOMINAL_ATTRIBUTES)) {
            result.add("-not-nominal-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.NUMERIC_ATTRIBUTES)) {
            result.add("-not-numeric-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.STRING_ATTRIBUTES)) {
            result.add("-not-string-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.DATE_ATTRIBUTES)) {
            result.add("-not-date-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.RELATIONAL_ATTRIBUTES)) {
            result.add("-not-relational-atts");
        }
        if (this.isEnabledNot(Capabilities.Capability.MISSING_VALUES)) {
            result.add("-not-missing-att-values");
        }
        if (this.isEnabled(Capabilities.Capability.ONLY_MULTIINSTANCE)) {
            result.add("-only-multi-instance");
        }
        if (this.getHandler() != null) {
            result.add("-W");
            result.add(this.getHandler().getClass().getName());
            if (this.getHandler() instanceof OptionHandler) {
                result.add("--");
                final String[] options = ((OptionHandler)this.getHandler()).getOptions();
                for (int i = 0; i < options.length; ++i) {
                    result.add(options[i]);
                }
            }
        }
        else if (this.getFilename().length() != 0) {
            result.add("-t");
            result.add(this.getFilename());
            result.add("-c");
            result.add(this.m_ClassIndex.getSingleIndex());
        }
        if (this.m_Superclass.length() != 0) {
            result.add("-superclass");
            result.add(this.m_Superclass);
        }
        else {
            result.add("-packages");
            result.add(this.m_Packages.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
        }
        return result.toArray(new String[result.size()]);
    }
    
    public void setHandler(final CapabilitiesHandler value) {
        this.m_Handler = value;
        this.setCapabilities(this.m_Handler.getCapabilities());
    }
    
    public CapabilitiesHandler getHandler() {
        return this.m_Handler;
    }
    
    public void setFilename(final String value) {
        this.m_Filename = value;
        if (this.m_Filename.length() != 0) {
            try {
                final Instances insts = new Instances(new BufferedReader(new FileReader(this.m_Filename)));
                this.m_ClassIndex.setUpper(insts.numAttributes());
                insts.setClassIndex(Integer.parseInt(this.getClassIndex()) - 1);
                this.setCapabilities(Capabilities.forInstances(insts));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String getFilename() {
        return this.m_Filename;
    }
    
    public void setClassIndex(final String value) {
        if (value.equals("-1")) {
            this.m_ClassIndex = null;
        }
        else {
            this.m_ClassIndex = new SingleIndex(value);
        }
    }
    
    public String getClassIndex() {
        if (this.m_ClassIndex == null) {
            return "-1";
        }
        return "" + this.m_ClassIndex.getIndex();
    }
    
    public void enable(final Capabilities.Capability c) {
        this.m_Capabilities.enable(c);
    }
    
    public boolean isEnabled(final Capabilities.Capability c) {
        return this.m_Capabilities.handles(c);
    }
    
    public void disable(final Capabilities.Capability c) {
        this.m_Capabilities.disable(c);
    }
    
    public void enableNot(final Capabilities.Capability c) {
        this.m_NotCapabilities.enable(c);
    }
    
    public boolean isEnabledNot(final Capabilities.Capability c) {
        return this.m_NotCapabilities.handles(c);
    }
    
    public void disableNot(final Capabilities.Capability c) {
        this.m_NotCapabilities.disable(c);
    }
    
    public boolean handles(final Capabilities.Capability c) {
        return this.m_Capabilities.handles(c);
    }
    
    @Override
    public Capabilities getCapabilities() {
        return this.m_Capabilities;
    }
    
    public void setCapabilities(final Capabilities c) {
        this.m_Capabilities = (Capabilities)c.clone();
    }
    
    public Capabilities getNotCapabilities() {
        return this.m_NotCapabilities;
    }
    
    public void setNotCapabilities(final Capabilities c) {
        this.m_NotCapabilities = (Capabilities)c.clone();
    }
    
    public Vector<String> getMatches() {
        return this.m_Matches;
    }
    
    public Vector<String> getMisses() {
        return this.m_Misses;
    }
    
    public Vector<String> find() {
        this.m_Matches = new Vector<String>();
        this.m_Misses = new Vector<String>();
        final Vector<String> list = ClassDiscovery.find(this.m_Superclass, this.m_Packages.toArray(new String[this.m_Packages.size()]));
        for (int i = 0; i < list.size(); ++i) {
            try {
                final Class cls = Class.forName(list.get(i));
                final Object obj = cls.newInstance();
                if (cls != this.getClass()) {
                    if (obj instanceof CapabilitiesHandler) {
                        final CapabilitiesHandler handler = (CapabilitiesHandler)obj;
                        final Capabilities caps = handler.getCapabilities();
                        boolean fits = true;
                        for (final Capabilities.Capability cap : Capabilities.Capability.values()) {
                            if (this.m_Capabilities.handles(cap) && !caps.handles(cap)) {
                                fits = false;
                                break;
                            }
                        }
                        if (!fits) {
                            this.m_Misses.add(list.get(i));
                        }
                        else {
                            for (final Capabilities.Capability cap : Capabilities.Capability.values()) {
                                if (this.m_NotCapabilities.handles(cap) && caps.handles(cap)) {
                                    fits = false;
                                    break;
                                }
                            }
                            if (!fits) {
                                this.m_Misses.add(list.get(i));
                            }
                            else if (caps.getMinimumNumberInstances() > this.m_Capabilities.getMinimumNumberInstances()) {
                                this.m_Misses.add(list.get(i));
                            }
                            else {
                                this.m_Matches.add(list.get(i));
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
        return this.m_Matches;
    }
    
    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 8034 $");
    }
    
    public static void main(final String[] args) {
        boolean printMisses = false;
        try {
            final FindWithCapabilities find = new FindWithCapabilities();
            try {
                printMisses = Utils.getFlag("misses", args);
                find.setOptions(args);
                Utils.checkForRemainingOptions(args);
            }
            catch (Exception ex) {
                String result = ex.getMessage() + "\n\n" + find.getClass().getName().replaceAll(".*\\.", "") + " Options:\n\n";
                final Enumeration enm = find.listOptions();
                while (enm.hasMoreElements()) {
                    final Option option = enm.nextElement();
                    result = result + option.synopsis() + "\n" + option.description() + "\n";
                }
                throw new Exception(result);
            }
            System.out.println("\nSearching for the following Capabilities:");
            System.out.print("- allowed: ");
            Iterator iter = find.getCapabilities().capabilities();
            boolean first = true;
            while (iter.hasNext()) {
                if (!first) {
                    System.out.print(", ");
                }
                first = false;
                System.out.print(iter.next());
            }
            System.out.println();
            System.out.print("- not allowed: ");
            iter = find.getNotCapabilities().capabilities();
            first = true;
            if (iter.hasNext()) {
                while (iter.hasNext()) {
                    if (!first) {
                        System.out.print(", ");
                    }
                    first = false;
                    System.out.print(iter.next());
                }
                System.out.println();
            }
            else {
                System.out.println("-");
            }
            find.find();
            Vector<String> list = find.getMatches();
            if (list.size() == 1) {
                System.out.println("\nFound " + list.size() + " class that matched the criteria:\n");
            }
            else {
                System.out.println("\nFound " + list.size() + " classes that matched the criteria:\n");
            }
            for (int i = 0; i < list.size(); ++i) {
                System.out.println(list.get(i));
            }
            if (printMisses) {
                list = find.getMisses();
                if (list.size() == 1) {
                    System.out.println("\nFound " + list.size() + " class that didn't match the criteria:\n");
                }
                else {
                    System.out.println("\nFound " + list.size() + " classes that didn't match the criteria:\n");
                }
                for (int i = 0; i < list.size(); ++i) {
                    System.out.println(list.get(i));
                }
            }
            System.out.println();
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
