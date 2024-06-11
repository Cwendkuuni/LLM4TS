package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean bean;

    @Before
    public void setUp() {
        bean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        // Typical use case
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("attr1", "value1");
        config.addAttribute("attr2", "value2");
        
        bean.configure(config);
        Configuration actualConfig = bean.getConfiguration();
        
        assertEquals(config, actualConfig);
    }

    @Test
    public void testConfigure_attributeNotExist() throws CheckstyleException {
        // Error scenario
        Configuration config = new DefaultConfiguration("testConfig");
        config.addAttribute("attr1", "value1");
        config.addAttribute("attr2", "value2");
        
        try {
            bean.configure(config);
            fail("Expected CheckstyleException was not thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = "Property 'attr3' in module testConfig does not exist, please check the documentation";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        // Typical use case
        Context context = new DefaultContext();
        context.put("attr1", "value1");
        context.put("attr2", "value2");
        
        bean.contextualize(context);
        
        // Check if properties are copied correctly
        assertEquals("value1", bean.getAttr1());
        assertEquals("value2", bean.getAttr2());
    }

    @Test
    public void testContextualize_attributeNotExist() throws CheckstyleException {
        // Error scenario
        Context context = new DefaultContext();
        context.put("attr1", "value1");
        context.put("attr2", "value2");
        
        try {
            bean.contextualize(context);
            fail("Expected CheckstyleException was not thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = "Property 'attr3' in module com.puppycrawl.tools.checkstyle.api.AutomaticBean does not exist, please check the documentation";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        // No specific behavior to test as this method is empty
        // Just execute the method to cover the code
        bean.finishLocalSetup();
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        // Error scenario
        Configuration childConf = new DefaultConfiguration("childConfig");
        childConf.addAttribute("attr1", "value1");
        childConf.addAttribute("attr2", "value2");
        
        try {
            bean.setupChild(childConf);
            fail("Expected CheckstyleException was not thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = "childConfig is not allowed as a child in testConfig";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}