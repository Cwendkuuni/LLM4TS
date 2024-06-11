package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    private File mockFile;

    @Before
    public void setUp() {
        mockFile = mock(File.class);
    }

    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        Dataset expectedDataset = new DefaultDataset();
        // Add your expected instances to expectedDataset

        Dataset actualDataset = ARFFHandler.loadARFF(mockFile);

        assertEquals(expectedDataset.size(), actualDataset.size());
        // Add more assertions to compare expectedDataset with actualDataset
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        Dataset expectedDataset = new DefaultDataset();
        // Add your expected instances to expectedDataset

        int classIndex = 2;
        Dataset actualDataset = ARFFHandler.loadARFF(mockFile, classIndex);

        assertEquals(expectedDataset.size(), actualDataset.size());
        // Add more assertions to compare expectedDataset with actualDataset
    }
}