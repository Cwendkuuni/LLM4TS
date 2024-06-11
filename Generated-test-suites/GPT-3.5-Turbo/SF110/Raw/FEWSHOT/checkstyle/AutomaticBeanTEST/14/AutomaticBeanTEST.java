
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        config.setAttribute("attribute1", "value1");
        config.setAttribute("attribute2", "value2");
        AutomaticBean.ConfigurableBean configurableBean = new AutomaticBean.ConfigurableBean();
        automaticBean.configure(config, configurableBean);

        assertNotNull(automaticBean.getConfiguration());
        assertEquals("value1", configurableBean.getAttribute1());
        assertEquals("value2", configurableBean.getAttribute2());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidProperty() {
        Configuration config = new Configuration();
        config.setAttribute("nonExistingAttribute", "value");
        automaticBean.configure(config, new AutomaticBean.ConfigurableBean());
    }

    @Test
    public void testConfigure_NoAttributes() {
        Configuration config = new Configuration();
        automaticBean.configure(config, new AutomaticBean.ConfigurableBean());

        assertNotNull(automaticBean.getConfiguration());
        assertNull(automaticBean.getConfiguration().getAttributes());
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", "value2");
        automaticBean.contextualize(context);

        AutomaticBean.ConfigurableBean configurableBean = new AutomaticBean.ConfigurableBean();
        automaticBean.configure(automaticBean.getConfiguration(), configurableBean);

        assertNotNull(automaticBean.getConfiguration());
        assertEquals("value1", configurableBean.getAttribute1());
        assertEquals("value2", configurableBean.getAttribute2());
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidProperty() {
        Context context = new Context();
        context.setAttribute("nonExistingAttribute", "value");
        automaticBean.contextualize(context);
    }

    @Test
    public void testContextualize_NoAttributes() {
        Context context = new Context();
        automaticBean.contextualize(context);

        assertNotNull(automaticBean.getConfiguration());
        assertNull(automaticBean.getConfiguration().getAttributes());
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        AutomaticBean automaticBeanSpy = Mockito.spy(automaticBean);
        automaticBeanSpy.finishLocalSetup();

        // Add assertions if there are any specific operations in finishLocalSetup()
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        AutomaticBean.GetChildConfiguration childConf = new AutomaticBean.GetChildConfiguration();
        automaticBean.setupChild(childConf);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_InvalidChild() throws CheckstyleException {
        Configuration childConf = new Configuration();
        automaticBean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter_convert() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, "value1, value2");

        assertNotNull(result);
        assertEquals(2, ((String[]) result).length);
        assertEquals("value1", ((String[]) result)[0]);
        assertEquals("value2", ((String[]) result)[1]);
    }

    @Test(expected = ConversionException.class)
    public void testRelaxedStringArrayConverter_convert_InvalidInput() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        converter.convert(String[].class, null);
    }
}