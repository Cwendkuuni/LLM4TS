
package net.sf.javaml.tools.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ARFFHandlerTest {

    private String arffFilePath;
    private int classIndex;
    private Dataset expectedDataset;

    public ARFFHandlerTest(String arffFilePath, int classIndex, Dataset expectedDataset) {
        this.arffFilePath = arffFilePath;
        this.classIndex = classIndex;
        this.expectedDataset = expectedDataset;
    }

    @Parameterized.Parameters
    public static Collection testCases() {
        return Arrays.asList(new Object[][]{
                {"testData1.arff", -1, createExpectedDataset1()},
                {"testData2.arff", 1, createExpectedDataset2()},
                {"testData3.arff", 0, createExpectedDataset3()}
        });
    }

    private static Dataset createExpectedDataset1() {
        Dataset dataset = new DefaultDataset();
        double[] values1 = {1.0, 2.0, 3.0};
        double[] values2 = {4.0, 5.0, 6.0};
        dataset.add(new DenseInstance(values1, "A"));
        dataset.add(new DenseInstance(values2, "B"));
        return dataset;
    }

    private static Dataset createExpectedDataset2() {
        Dataset dataset = new DefaultDataset();
        double[] values1 = {10.0, 20.0, 30.0};
        double[] values2 = {40.0, 50.0, 60.0};
        dataset.add(new DenseInstance(values1, "X"));
        dataset.add(new DenseInstance(values2, "Y"));
        return dataset;
    }

    private static Dataset createExpectedDataset3() {
        Dataset dataset = new DefaultDataset();
        double[] values1 = {100.0, 200.0};
        double[] values2 = {400.0, 500.0};
        dataset.add(new DenseInstance(values1, "I"));
        dataset.add(new DenseInstance(values2, "J"));
        return dataset;
    }

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        Dataset loadedDataset = ARFFHandler.loadARFF(new File(arffFilePath));
        assertEquals(expectedDataset, loadedDataset);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        Dataset loadedDataset = ARFFHandler.loadARFF(new File(arffFilePath), classIndex);
        assertEquals(expectedDataset, loadedDataset);
    }
}
