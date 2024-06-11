package com.fasterxml.jackson.core.base;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GeneratorBaseTest {
  
    private GeneratorBase classUnderTest;
  
    @Before
    public void setup() {
        classUnderTest = Mockito.mock(GeneratorBase.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testSetFeatureMask() {
        classUnderTest.setFeatureMask(0);
    }

    @Test
    public void testOverrideStdFeatures() {
        classUnderTest.overrideStdFeatures(0, 0);
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        classUnderTest.useDefaultPrettyPrinter();
    }

    @Test
    public void testSetCodec() {
        classUnderTest.setCodec(null);
    }

    @Test
    public void testGetCodec() {
        classUnderTest.getCodec();
    }
}