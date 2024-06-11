
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import java.util.Locale;

public class AutomaticBeanTest {

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNotNullChildConf() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setupChild(new Configuration());
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "key", "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNonExistingProperty() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "unknownKey", "value", true);
    }

    @Test
    public void testTryCopyPropertyWithIllegalValue() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();

        try {
            automaticBean.tryCopyProperty("moduleName", "key", "illegalValue", true);
        } catch (CheckstyleException e) {
            assertEquals("illegal value 'illegalValue' for property 'key' of module moduleName", e.getMessage());
            assertEquals(ConversionException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        configuration.addAttribute("attribute", "value");
        configuration.addChild(new Configuration());
        
        automaticBean.configure(configuration);
    }
    
    @Test
    public void testContextualize() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.addAttribute("attribute", "value");

        automaticBean.contextualize(context);
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        assertNull(automaticBean.getConfiguration());
    }

    @Test
    public void testRelaxedStringArrayConverter() throws CheckstyleException {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();

        Object result = converter.convert(String.class, "abc,def,ghi");
        assertArrayEquals(new String[]{"abc", "def", "ghi"}, (Object[])result);

        result = converter.convert(String.class, "1,2,3,4,5");
        assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, (Object[])result);

        result = converter.convert(String.class, "one,two,three");
        assertArrayEquals(new String[]{"one", "two", "three"}, (Object[])result);
    }
}
