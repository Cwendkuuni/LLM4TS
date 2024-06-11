import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testConstructor() {
        // Test constructor with features and codec
        ObjectCodec mockCodec = mock(ObjectCodec.class);
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), mockCodec);
        assertNotNull(generator);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator._features);
        assertEquals(mockCodec, generator._objectCodec);
        assertNotNull(generator._writeContext);
        assertFalse(generator._cfgNumbersAsStrings);
    }

    @Test
    public void testEnableFeature() {
        // Test enable feature method
        GeneratorBase generator = mock(GeneratorBase.class);
        when(generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS)).thenReturn(generator);
        
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        
        verify(generator, times(1)).enable(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    // Add more test cases for each public method in GeneratorBase class

}