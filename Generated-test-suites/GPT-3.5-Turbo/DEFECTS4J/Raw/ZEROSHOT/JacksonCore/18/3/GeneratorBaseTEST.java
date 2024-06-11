package com.fasterxml.jackson.core.base;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.util.VersionUtil;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    private GeneratorBase generatorBase;

    @Before
    public void setup() {
        generatorBase = new GeneratorBaseMock();
    }

    @Test
    public void testVersion() {
        assertEquals(VersionUtil.versionFor(GeneratorBaseMock.class), generatorBase.version());
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(generatorBase.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        generatorBase.setCurrentValue("test");
        assertEquals("test", generatorBase.getCurrentValue());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() {
        assertEquals(0, generatorBase.getFeatureMask());
    }

    @Test
    public void testEnable() {
        generatorBase.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable() {
        generatorBase.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        generatorBase.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testSetFeatureMask() {
        generatorBase.setFeatureMask(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testOverrideStdFeatures() {
        generatorBase.setFeatureMask(0);
        generatorBase.overrideStdFeatures(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generatorBase.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testUseDefaultPrettyPrinterSuccess() {
        assertNull(generatorBase.getPrettyPrinter());
        assertTrue(generatorBase.useDefaultPrettyPrinter() instanceof DefaultPrettyPrinter);
    }

    @Test
    public void testUseDefaultPrettyPrinterFailure() {
        DefaultPrettyPrinter dpp = new DefaultPrettyPrinter();
        generatorBase.setPrettyPrinter(dpp);
        assertNotNull(generatorBase.getPrettyPrinter());
        assertTrue(generatorBase.useDefaultPrettyPrinter() instanceof DefaultPrettyPrinter);
        assertEquals(dpp, generatorBase.getPrettyPrinter());
    }
}


class GeneratorBaseMock extends GeneratorBase {
    public GeneratorBaseMock() {
        super(0, null);
    }

    @Override
    protected void _releaseBuffers() {
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException {
    }

    @Override
    protected PrettyPrinter _constructDefaultPrettyPrinter() {
        return null;
    }

    @Override
    protected String _asString(BigDecimal value) throws IOException {
        return null;
    }

    @Override
    public void flush() throws IOException {
    }
}