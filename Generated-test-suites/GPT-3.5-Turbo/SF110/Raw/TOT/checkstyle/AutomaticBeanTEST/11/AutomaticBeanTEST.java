package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure_withEmptyConfig_shouldNotThrowException() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        automaticBean.configure(config);

        // Assertion
        assertNotNull(automaticBean.getConfiguration());
        assertEquals("testConfig", automaticBean.getConfiguration().getName());
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_withInvalidConfigKey_shouldThrowException() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("invalidKey", "invalidValue");
        
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException ex) {
            assertEquals("Property 'invalidKey' in module testConfig does not exist, please check the documentation", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_withInvalidConfigValue_shouldThrowException() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("testKey", "invalidValue");

        try {
            automaticBean.configure(config);
        } catch (CheckstyleException ex) {
            assertEquals("illegal value 'invalidValue' for property 'testKey' of module testConfig", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_withInvalidChildConfig_shouldThrowException() throws CheckstyleException {
        Configuration childConf = new DefaultConfiguration("childConfig");

        try {
            automaticBean.setupChild(childConf);
        } catch (CheckstyleException ex) {
            assertEquals("childConfig is not allowed as a child in null", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

    @Test
    public void testContextualize_withEmptyContext_shouldNotThrowException() throws CheckstyleException {
        Context context = new SimpleContext();

        automaticBean.contextualize(context);

        // Assertion
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(AutomaticBean.class.getName(), automaticBean.getConfiguration().getName());
    }

    @Test
    public void testTryCopyProperty_withValidProperty_shouldCopyPropertyToBean() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("testKey", "testValue");

        automaticBean.configure(config);

        // Assertion
        assertEquals("testValue", automaticBean.getConfiguration().getAttribute("testKey"));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_withInvalidProperty_shouldThrowException() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("testKey", "testValue");

        try {
            automaticBean.tryCopyProperty(config.getName(), "invalidKey", "invalidValue", true);
        } catch (CheckstyleException ex) {
            assertEquals("Property 'invalidKey' in module testConfig does not exist, please check the documentation", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_withInvalidValue_shouldThrowException() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("testKey", "testValue");

        try {
            automaticBean.tryCopyProperty(config.getName(), "testKey", "invalidValue", true);
        } catch (CheckstyleException ex) {
            assertEquals("illegal value 'invalidValue' for property 'testKey' of module testConfig", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

    @Test
    public void testGetConfiguration_withConfiguredInstance_shouldReturnConfiguration() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("testConfig");
        automaticBean.configure(config);

        // Assertion
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup_withEmptyImplementation_shouldNotThrowException() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // No assertion as this method is empty
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_withNonNullChildConfig_shouldThrowException() throws CheckstyleException {
        Configuration childConf = new DefaultConfiguration("childConfig");

        try {
            automaticBean.setupChild(childConf);
        } catch (CheckstyleException ex) {
            assertEquals("childConfig is not allowed as a child in null", ex.getMessage());
            throw ex;
        }

        // Assertion - This should not be reached
        fail("Expected CheckstyleException to be thrown");
    }

}