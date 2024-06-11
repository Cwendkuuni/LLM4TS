import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.configure(config);
        } catch (CheckstyleException e) {
            fail("Exception thrown during configuration");
        }
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.contextualize(context);
        } catch (CheckstyleException e) {
            fail("Exception thrown during contextualization");
        }
    }

    @Test
    public void testFinishLocalSetup() {
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Exception thrown during local setup");
        }
    }

    @Test
    public void testSetupChild() {
        Configuration childConf = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        try {
            bean.setupChild(childConf);
            fail("No exception thrown for invalid child configuration");
        } catch (CheckstyleException e) {
            // Successfully caught exception
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Class<?> type = String[].class;
        Object value = "value1, value2, value3";
        Object result = converter.convert(type, value);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("value1", arrayResult[0]);
        assertEquals("value2", arrayResult[1]);
        assertEquals("value3", arrayResult[2]);
    }

}