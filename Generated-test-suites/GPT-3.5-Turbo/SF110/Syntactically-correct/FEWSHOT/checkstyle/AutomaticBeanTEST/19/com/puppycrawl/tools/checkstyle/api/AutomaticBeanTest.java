package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new DefaultConfiguration("test");
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyNonExistingKey() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new DefaultConfiguration("test");
        configuration.addAttribute("nonExistingKey", "value");
        automaticBean.configure(configuration);
    }

    @Test
    public void testTryCopyProperty() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new DefaultConfiguration("test");
        configuration.addAttribute("existingKey", "value");
        automaticBean.configure(configuration);
        assertEquals("value", PropertyUtils.getProperty(automaticBean, "existingKey"));
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new DefaultConfiguration("test");
        configuration.addAttribute("existingKey", "invalidValue");
        try {
            automaticBean.configure(configuration);
        } catch (CheckstyleException e) {
            assertEquals("illegal value 'invalidValue' for property 'existingKey' of module test", e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new DefaultContext();
        context.put("key", "value");
        automaticBean.contextualize(context);
        assertEquals("value", PropertyUtils.getProperty(automaticBean, "key"));
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetup() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean() {
            @Override
            protected void finishLocalSetup() throws CheckstyleException {
                throw new CheckstyleException("Local setup finished");
            }
        };
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean() {
            @Override
            protected void setupChild(Configuration childConf) throws CheckstyleException {
                throw new CheckstyleException("Child setup called");
            }
        };
        automaticBean.setupChild(null);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        String input = "one, two,three ";
        Object output = converter.convert(String[].class, input);
        assertEquals(Arrays.asList("one", "two", "three"), Arrays.asList((String[]) output));
    }
}