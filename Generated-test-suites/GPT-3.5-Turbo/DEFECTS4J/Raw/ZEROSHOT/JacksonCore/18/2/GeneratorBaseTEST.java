import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GeneratorBaseTest {

    private GeneratorBase generatorBase = mock(GeneratorBase.class);

    @Test
    public void testVersion() {
        assertNotNull(generatorBase.version());
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(generatorBase.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        generatorBase.setCurrentValue(value);
        assertEquals(value, generatorBase.getCurrentValue());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testGetFeatureMask() {
        assertEquals(0, generatorBase.getFeatureMask());
    }

    @Test
    public void testEnable() {
        generatorBase.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testDisable() {
        generatorBase.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        assertFalse(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testSetFeatureMask() {
        generatorBase.setFeatureMask(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask());
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testOverrideStdFeatures() {
        generatorBase.overrideStdFeatures(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask(),
                JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask());
        assertTrue(generatorBase.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        JsonGenerator jsonGenerator = generatorBase.useDefaultPrettyPrinter();
        assertNotNull(jsonGenerator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        generatorBase.setCodec(objectCodec);
        assertEquals(objectCodec, generatorBase.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        assertNotNull(generatorBase.getOutputContext());
    }

    // TODO Rest of the test methods

    @Test
    public void testFlush() throws IOException {
        generatorBase.flush();
        verify(generatorBase).flush();
    }

    @Test
    public void testClose() throws IOException {
        generatorBase.close();
        assertTrue(generatorBase.isClosed());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(generatorBase.isClosed());
    }

    @Test
    public void testReleaseBuffers() {
        generatorBase._releaseBuffers();
        verify(generatorBase).releaseBuffers();
    }

    @Test
    public void testVerifyValueWrite() throws IOException {
        generatorBase._verifyValueWrite("test");
        verify(generatorBase).verifyValueWrite("test");
    }

    @Test
    public void testConstructDefaultPrettyPrinter() {
        assertNotNull(generatorBase._constructDefaultPrettyPrinter());
    }

    @Test
    public void testAsString() throws IOException {
        BigDecimal value = new BigDecimal("10.0");
        assertEquals("10.0", generatorBase._asString(value));
    }

    @Test
    public void testDecodeSurrogate() throws IOException {
        int surr1 = 0xD800;
        int surr2 = 0xDC00;
        assertEquals(65536, generatorBase.decodeSurrogate(surr1, surr2));
    }
}