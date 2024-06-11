package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AutomaticBeanTest {

    private static AutomaticBean automaticBean;

    @BeforeClass
    public static void setup() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();

        // Test case 1: Empty configuration
        automaticBean.configure(config);
        assertNull(automaticBean.getConfiguration());

        // Test case 2: Configuration with attributes
        config.addAttribute("attribute1", "value1");
        config.addAttribute("attribute2", "value2");
        automaticBean.configure(config);
        assertEquals(2, automaticBean.getConfiguration().getAttributeNames().length);

        // Test case 3: Configuration with children
        Configuration childConfig1 = new Configuration();
        childConfig1.setName("childConfig1");
        Configuration childConfig2 = new Configuration();
        childConfig2.setName("childConfig2");
        config.addChild(childConfig1);
        config.addChild(childConfig2);
        automaticBean.configure(config);
        assertEquals(2, automaticBean.getConfiguration().getChildren().length);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();

        // Test case 1: Empty context
        automaticBean.contextualize(context);

        // Test case 2: Context with attributes
        context.addAttribute("attribute1", "value1");
        context.addAttribute("attribute2", "value2");
        automaticBean.contextualize(context);

        // Test case 3: Context with null attribute
        context.addAttribute("nullAttribute", null);
        try {
            automaticBean.contextualize(context);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("Cannot set property 'nullAttribute' to 'null' in module com.puppycrawl.tools.checkstyle" +
                    ".api.AutomaticBean", e.getMessage());
        }
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // No assertion, method does not return anything
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = new Configuration();
        childConf.setName("childConf");

        // Test case 1: Non-null childConf
        try {
            automaticBean.setupChild(childConf);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("childConf is not allowed as a child in null", e.getMessage());
        }

        // Test case 2: Null childConf
        automaticBean.setupChild(null);
        // No assertion, method does not return anything
    }

    @Test
    public void testTryCopyProperty() throws CheckstyleException, IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean);

        PropertyDescriptor descriptor = new PropertyDescriptor("configuration", AutomaticBean.class);
        beanUtilsBean.copyProperty(automaticBean, "configuration", new Configuration());

        // Test case 1: Rechecking property that does not exist
        Class<?> superClass = automaticBean.getClass().getSuperclass();
        Field field = superClass.getDeclaredField("BeanUtilsBean");
        field.setAccessible(true);
        field.set(beanUtilsBean, null);
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", "value", true);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("Property 'nonExistentProperty' in module moduleName does not exist, please check the " +
                    "documentation", e.getMessage());
        } finally {
            field.set(beanUtilsBean, createBeanUtilsBean());
        }

        // Test case 2: Copy property with InvocationTargetException
        try {
            automaticBean.tryCopyProperty("moduleName", "configuration", new Configuration(), false);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("Cannot set property 'configuration' to 'Configuration@4bc22144' in module moduleName",
                    e.getMessage());
        }

        // Test case 3: Copy property with ConversionException
        try {
            automaticBean.tryCopyProperty("moduleName", "configuration", "invalidValue", false);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            assertEquals("illegal value 'invalidValue' for property 'configuration' of module moduleName",
                    e.getMessage());
        }
    }
}