import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class SampleTest {

    private Sample sample;

    @Before
    public void setUp() throws Exception {
        sample = new Sample();
    }

    @After
    public void tearDown() throws Exception {
        sample = null;
    }

    @Test
    public void testSampleMethod() {
        String result = sample.sampleMethod("input");
        assertEquals("Expected result", result);
    }
}