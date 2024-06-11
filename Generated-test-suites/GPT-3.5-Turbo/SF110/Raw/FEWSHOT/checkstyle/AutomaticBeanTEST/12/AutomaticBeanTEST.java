package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.apache.commons.beanutils.converters.*;
import java.lang.reflect.*;
import org.apache.commons.beanutils.*;
import java.beans.*;
import com.google.common.collect.*;
import java.util.*;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        assertNotNull(AutomaticBean.createBeanUtilsBean());
    }

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testTryCopyProperty() {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setConfiguration(new Configuration());

        // Test property does not exist
        try {
            automaticBean.tryCopyProperty("module1", "property1", "value1", true);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("Property 'property1' in module module1 does not exist, please check the documentation", e.getMessage());
        }

        // Test property exists
        try {
            automaticBean.tryCopyProperty("module2", "property2", "value2", true);
        } catch (CheckstyleException e) {
            fail("Unexpected CheckstyleException");
        }
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        context.addAttribute("attribute1", "value1");
        context.addAttribute("attribute2", "value2");

        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Unexpected CheckstyleException");
        }

        assertNull(automaticBean.getConfiguration().getAttribute("attribute1"));
        assertNull(automaticBean.getConfiguration().getAttribute("attribute2"));
    }

    @Test
    public void testGetConfiguration() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setConfiguration(config);
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() {
        AutomaticBean automaticBean = new AutomaticBean();
        try {
            automaticBean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Unexpected CheckstyleException");
        }
    }

    @Test
    public void testSetupChild() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration("childConfig");
        try {
            automaticBean.setupChild(childConfig);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("childConfig is not allowed as a child in null", e.getMessage());
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        // Test empty string
        Object result = converter.convert(String[].class, "");
        assertTrue(result instanceof String[]);
        assertEquals(0, ((String[]) result).length);

        // Test single value
        result = converter.convert(String[].class, "value1");
        assertTrue(result instanceof String[]);
        assertEquals(1, ((String[]) result).length);
        assertEquals("value1", ((String[]) result)[0]);

        // Test comma-separated values
        result = converter.convert(String[].class, "value1, value2, value3");
        assertTrue(result instanceof String[]);
        assertEquals(3, ((String[]) result).length);
        assertEquals("value1", ((String[]) result)[0]);
        assertEquals("value2", ((String[]) result)[1]);
        assertEquals("value3", ((String[]) result)[2]);
    }
}