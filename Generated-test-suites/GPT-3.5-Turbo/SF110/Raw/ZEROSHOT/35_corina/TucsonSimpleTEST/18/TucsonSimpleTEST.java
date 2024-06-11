
package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import corina.*;
import corina.ui.*;
import corina.util.*;

import java.io.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple ts = new TucsonSimple();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, ts.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple ts = new TucsonSimple();
        String expected = ".RWL";
        assertEquals(expected, ts.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple ts = new TucsonSimple();
        BufferedReader mockReader = mock(BufferedReader.class);

        ts.load(mockReader);
    }

    @Test
    public void testMake6digitCode_withIdInMeta() {
        TucsonSimple ts = new TucsonSimple();
        Sample mockSample = mock(Sample.class);
        when(mockSample.meta.containsKey(eq("id"))).thenReturn(true);
        when(mockSample.meta.get(eq("id"))).thenReturn("123456");

        String result = ts.make6digitCode(mockSample);

        assertEquals("123456", result);
    }

    @Test
    public void testMake6digitCode_withoutIdInMeta() {
        TucsonSimple ts = new TucsonSimple();
        Sample mockSample = mock(Sample.class);
        when(mockSample.meta.containsKey(eq("id"))).thenReturn(false);

        String result = ts.make6digitCode(mockSample);

        assertEquals("000000", result);
    }

    // Add the remaining test methods for the other public and private methods

}

