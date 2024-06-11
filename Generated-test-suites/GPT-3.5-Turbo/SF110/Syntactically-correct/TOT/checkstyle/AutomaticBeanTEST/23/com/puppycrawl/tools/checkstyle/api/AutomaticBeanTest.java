package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure_WhenAttributeExists_ShouldCopyProperty() throws CheckstyleException {
        // Arrange
        Configuration configuration = new DefaultConfiguration("test");
        configuration.addAttribute("key", "value");

        // Act
        automaticBean.configure(configuration);

        // Assert
        assertNotNull(automaticBean.getConfiguration());
        assertEquals("value", automaticBean.getConfiguration().getAttribute("key"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_WhenAttributeDoesNotExist_ShouldThrowCheckstyleException() throws CheckstyleException {
        // Arrange
        Configuration configuration = new DefaultConfiguration("test");
        configuration.addAttribute("invalidKey", "value");

        // Act
        automaticBean.configure(configuration);

        // Assert
        // Expected CheckstyleException
    }

    @Test
    public void testConfigure_WhenChildConfigurationExists_ShouldSetupChild() throws CheckstyleException {
        // Arrange
        Configuration childConfiguration = new DefaultConfiguration("child");

        // Act
        automaticBean.setupChild(childConfiguration);

        // Assert
        // Expected CheckstyleException
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_WhenChildConfigurationIsNull_ShouldThrowCheckstyleException() throws CheckstyleException {
        // Arrange
        Configuration childConfiguration = null;

        // Act
        automaticBean.setupChild(childConfiguration);

        // Assert
        // Expected CheckstyleException
    }

    @Test
    public void testContextualize_WhenAttributeExists_ShouldCopyProperty() throws CheckstyleException {
        // Arrange
        Context context = new DummyContext();
        context.put("key", "value");

        // Act
        automaticBean.contextualize(context);

        // Assert
        assertNotNull(automaticBean.getConfiguration());
        assertEquals("value", automaticBean.getConfiguration().getAttribute("key"));
    }

    @Test
    public void testFinishLocalSetup_ShouldNotThrowCheckstyleException() throws CheckstyleException {
        // Arrange
        // No specific arrangement needed

        // Act
        automaticBean.finishLocalSetup();

        // Assert
        // Expected no CheckstyleException
    }

    @Test
    public void testRelaxedStringArrayConverter_WhenValidString_ShouldConvertToArray() {
        // Arrange
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        // Act
        Object result = converter.convert(String[].class, "value1, value2,value3");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("value1", arrayResult[0]);
        assertEquals("value2", arrayResult[1]);
        assertEquals("value3", arrayResult[2]);
    }

    @Test
    public void testRelaxedStringArrayConverter_WhenEmptyString_ShouldConvertToEmptyArray() {
        // Arrange
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        // Act
        Object result = converter.convert(String[].class, "");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(0, arrayResult.length);
    }

    @Test
    public void testRelaxedStringArrayConverter_WhenNullValue_ShouldConvertToEmptyArray() {
        // Arrange
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        // Act
        Object result = converter.convert(String[].class, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(0, arrayResult.length);
    }

}