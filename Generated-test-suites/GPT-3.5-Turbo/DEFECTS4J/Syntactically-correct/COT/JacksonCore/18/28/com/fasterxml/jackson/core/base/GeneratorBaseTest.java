import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorBaseTest {

    @Test
    public void testVersion() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertNotNull(generator.version());
    }

    @Test
    public void testGetCurrentValue() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        generator.setCurrentValue("test");
        assertEquals("test", generator.getCurrentValue());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBaseTestImpl(4, null);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBaseTestImpl(4, null);
        assertEquals(4, generator.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new GeneratorBaseTestImpl(4, null);
        assertEquals(generator, generator.enable(GeneratorBase.Feature.ESCAPE_NON_ASCII));
        assertEquals(12, generator.getFeatureMask());
    }

    @Test
    public void testDisable() {
        GeneratorBase generator = new GeneratorBaseTestImpl(12, null);
        assertEquals(generator, generator.disable(GeneratorBase.Feature.ESCAPE_NON_ASCII));
        assertEquals(4, generator.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertNotNull(generator.useDefaultPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        generator.setCodec(null);
        assertNull(generator.getCodec());
    }

    @Test
    public void testGetCodec() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertNull(generator.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertNotNull(generator.getOutputContext());
    }

    @Test
    public void testFlush() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertThrows(IOException.class, () -> generator.flush());
    }

    @Test
    public void testClose() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertThrows(IOException.class, () -> generator.close());
    }

    @Test
    public void testIsClosed() {
        GeneratorBase generator = new GeneratorBaseTestImpl(0, null);
        assertFalse(generator.isClosed());
        generator.close();
        assertTrue(generator.isClosed());
    }

    private static class GeneratorBaseTestImpl extends GeneratorBase {

        public GeneratorBaseTestImpl(int features, ObjectCodec codec) {
            super(features, codec);
        }

        @Override
        protected void _releaseBuffers() {

        }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException {

        }
    }
}