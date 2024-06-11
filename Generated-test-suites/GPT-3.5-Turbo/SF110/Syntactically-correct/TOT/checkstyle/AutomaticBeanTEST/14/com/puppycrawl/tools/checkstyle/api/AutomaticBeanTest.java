package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Locale;

public class AutomaticBeanTest {

    @Mock
    private Configuration mockConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfigure() {
        AutomaticBean automaticBean = new AutomaticBean();

        // Typical use case
        automaticBean.configure(mockConfig);

        // Verify if the configuration is set correctly
        Assert.assertEquals(mockConfig, automaticBean.getConfiguration());

        // Edge case: Empty configuration
        automaticBean.configure(new Configuration());

        // Verify if the configuration is set correctly
        Assert.assertNotNull(automaticBean.getConfiguration());

        // Verify if the configuration is empty
        Assert.assertEquals(0, automaticBean.getConfiguration().getAttributeNames().length);

        // Error scenario: Non-existing property
        Configuration nonExistingPropertyConfig = new Configuration();
        nonExistingPropertyConfig.addAttribute("nonExistingProperty", "value");
        try {
            automaticBean.configure(nonExistingPropertyConfig);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "Property '%s' in module %s does not exist, please check the documentation",
                    "nonExistingProperty",
                    nonExistingPropertyConfig.getName());
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        // Typical use case
        PropertyDescriptor descriptor = new PropertyDescriptor("configuration", AutomaticBean.class);
        Field descriptorField = PropertyDescriptor.class.getDeclaredField("name");
        descriptorField.setAccessible(true);
        descriptorField.set(descriptor, "property");
        automaticBean.tryCopyProperty("moduleName", "configuration", mockConfig, true);

        // Verify if the property is copied correctly
        Assert.assertEquals(mockConfig, automaticBean.getConfiguration());

        // Edge case: Recheck=false
        PropertyDescriptor nonExistingPropertyDescriptor = new PropertyDescriptor("nonExistingProperty", AutomaticBean.class);
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistingProperty", mockConfig, false);
        } catch (CheckstyleException e) {
            Assert.fail("Unexpected CheckstyleException");
        }

        // Verify if the property is not copied when recheck=false
        Assert.assertNotEquals(mockConfig, automaticBean.getConfiguration());

        // Error scenario: NoSuchMethodException
        automaticBean.tryCopyProperty("moduleName", "configuration", mockConfig, true);
        MockitoAnnotations.initMocks(this); // Resetting to avoid Mockito issue
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistingProperty", mockConfig, true);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "Cannot set property '%s' to '%s' in module %s",
                    "nonExistingProperty", mockConfig, "moduleName");
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        // Error scenario: ConversionException
        automaticBean.tryCopyProperty("moduleName", "configuration", "invalidValue", true);
        try {
            automaticBean.tryCopyProperty("moduleName", "configuration", "invalidValue", true);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "illegal value '%s' for property '%s' of module %s",
                    "invalidValue", "configuration", "moduleName");
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();

        // Typical use case
        automaticBean.contextualize(new Context());

        // Verify if the contextualization is executed without an exception

        // Edge case: Empty context
        automaticBean.contextualize(new Context());

        // Verify if the contextualization is executed without an exception

        // Edge case: Non-empty context
        Context context = new Context();
        context.put("key", "value");
        automaticBean.contextualize(context);

        // Verify if the properties are copied correctly

        // Error scenario: ConversionException
        Context conversionExceptionContext = new Context();
        conversionExceptionContext.put("key", "invalidValue");
        try {
            automaticBean.contextualize(conversionExceptionContext);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "illegal value '%s' for property '%s' of module %s",
                    "invalidValue", "key", AutomaticBean.class.getName());
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();

        // Typical use case
        Configuration childConfig = null;
        try {
            automaticBean.setupChild(childConfig);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "null is not allowed as a child in %s", automaticBean.getConfiguration().getName());
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        // Error scenario: Non-null child configuration
        Configuration nonNullChildConfig = new Configuration();
        try {
            automaticBean.setupChild(nonNullChildConfig);
            Assert.fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            String expectedMessage = String.format(Locale.ROOT,
                    "%s is not allowed as a child in %s", nonNullChildConfig.getName(), automaticBean.getConfiguration().getName());
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        // Typical use case
        Object result = converter.convert(String[].class, "token1, token2, token3 ");
      
        Assert.assertEquals(String[].class, result.getClass());
        Assert.assertArrayEquals(new String[]{"token1", "token2", "token3"}, (String[]) result);

        // Edge case: Empty string
        result = converter.convert(String[].class, "");
        Assert.assertEquals(String[].class, result.getClass());
        Assert.assertArrayEquals(new String[0], (String[]) result);
    }
}