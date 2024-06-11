
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("attribute1", "value1");
        config.addAttribute("attribute2", "value2");

        automaticBean.configure(config);

        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidProperty() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("invalidAttribute", "value");
        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testIllegalAccessException() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("attribute3", "value");
        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testIllegalArgumentException() throws CheckstyleException {
        Configuration config = new Configuration();
        config.addAttribute("attribute4", "value");
        automaticBean.configure(config);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", "value2");

        automaticBean.contextualize(context);

        assertEquals("value1", automaticBean.getAttribute1());
        assertEquals("value2", automaticBean.getAttribute2());
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // No assertions needed for this method
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = new Configuration();
        automaticBean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, "value1, value2, value3");
        assertTrue(result instanceof String[]);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, (String[]) result);
    }
}
