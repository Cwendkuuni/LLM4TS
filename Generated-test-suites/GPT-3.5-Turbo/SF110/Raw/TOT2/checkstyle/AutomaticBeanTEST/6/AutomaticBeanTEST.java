package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.beanutils.converters.*;
import org.junit.Before;

public class AutomaticBeanTest {

    // Create an instance of AutomaticBean for testing
    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() {
        // Test configuring the AutomaticBean with a valid Configuration
        Configuration config = new Configuration();
        config.addAttribute("key1", "value1");
        config.addAttribute("key2", "value2");
        automaticBean.configure(config);
        
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testTryCopyProperty() {
        // Test copying a property with a valid key and value
        automaticBean.configure(new Configuration());
        try {
            automaticBean.tryCopyProperty("moduleName", "key", "value", true);
        } catch (CheckstyleException e) {
            fail("Exception thrown for valid property copying");
        }
    }

    @Test
    public void testContextualize() {
        // Test contextualizing the AutomaticBean with a Context
        Context context = new Context();
        context.put("key1", "value1");
        context.put("key2", "value2");
        automaticBean.contextualize(context);

        // Test whether the properties were copied successfully
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() {
        // Test finishing local setup of the AutomaticBean
        try {
            automaticBean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Exception thrown while finishing local setup");
        }
    }

    @Test
    public void testSetupChild() {
        // Test setting up a child Configuration
        Configuration childConfig = new Configuration();
        childConfig.setName("ChildConfig");
        try {
            automaticBean.setupChild(childConfig);
        } catch (CheckstyleException e) {
            // Check whether the correct exception message is thrown
            assertEquals("ChildConfig is not allowed as a child in null", e.getMessage());
        }
    }
}