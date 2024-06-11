import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.io.*;
import java.util.*;

public class TucsonSimpleTest {

    @Mock
    private BufferedReader mockReader;
    
    @Mock
    private BufferedWriter mockWriter;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "TucsonSimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        try {
            tucsonSimple.load(mockReader);
            fail("Expected WrongFiletypeException");
        } catch (IOException e) {
            fail("Expected WrongFiletypeException");
        } catch (WrongFiletypeException e) {
            // success
        }
    }

    @Test
    public void testSaveData() {
        // Creating a Sample object for testing
        Sample sample = new Sample();
        sample.meta = new HashMap<String, Object>();
        sample.meta.put("id", "123456");
        sample.range = new Range(new Year(2000), new Year(2005));
        sample.data = new ArrayList<Integer>();
        sample.data.add(10);
        sample.data.add(20);
        sample.data.add(30);
        sample.data.add(40);
        sample.data.add(50);

        // Creating the expected output
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("1234562000");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");
        expectedOutput.append("1234562001");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");
        expectedOutput.append("1234562002");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");
        expectedOutput.append("1234562003");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");
        expectedOutput.append("1234562004");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");
        expectedOutput.append("1234562005");
        expectedOutput.append("      10");
        expectedOutput.append("      20");
        expectedOutput.append("      30");
        expectedOutput.append("      40");
        expectedOutput.append("      50\n");

        // Mocking the BufferedWriter
        try {
            Mockito.when(mockWriter.write(Mockito.anyString())).thenReturn(null);
            Mockito.when(mockWriter.newLine()).thenReturn(null);
        } catch (IOException e) {
            fail("IOException occurred during mocking the BufferedWriter");
        }

        TucsonSimple tucsonSimple = new TucsonSimple();
        try {
            tucsonSimple.saveData(sample, mockWriter);
        } catch (IOException e) {
            fail("IOException occurred during saveData");
        }

        try {
            Mockito.verify(mockWriter).write(expectedOutput.toString());
            Mockito.verify(mockWriter).newLine();
            Mockito.verify(mockWriter, Mockito.times(6)).write(Mockito.anyString());
            Mockito.verify(mockWriter, Mockito.times(6)).newLine();
        } catch (IOException e) {
            fail("IOException occurred during verifying the interactions of BufferedWriter");
        }
    }

    @Test
    public void testSave() {
        // Mocking the BufferedWriter
        try {
            Mockito.when(mockWriter.write(Mockito.anyString())).thenReturn(null);
            Mockito.when(mockWriter.newLine()).thenReturn(null);
        } catch (IOException e) {
            fail("IOException occurred during mocking the BufferedWriter");
        }

        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = Mockito.mock(Sample.class);
        try {
            tucsonSimple.save(sample, mockWriter);
        } catch (IOException e) {
            fail("IOException occurred during save");
        }

        try {
            Mockito.verify(mockWriter).newLine();
        } catch (IOException e) {
            fail("IOException occurred during verifying the interaction of BufferedWriter");
        }
    }
}