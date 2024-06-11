package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;

import org.junit.Test;

public class ARFFHandlerTest {
	@Test
	public void testLoadARFFFile() {
		File file = new File("test.arff");
		Dataset dataset;
		try {
			dataset = ARFFHandler.loadARFF(file);
			assertEquals(0, dataset.size());
		} catch (FileNotFoundException e) {
			// handle exception
		}		
	}

	@Test
	public void testLoadARFFFileWithClassIndex() {
		File file = new File("test.arff");
		int classIndex = 0;
		Dataset dataset;
		try {
			dataset = ARFFHandler.loadARFF(file, classIndex);
			assertEquals(0, dataset.size());
		} catch (FileNotFoundException e) {
			// handle exception
		}		
	}
}