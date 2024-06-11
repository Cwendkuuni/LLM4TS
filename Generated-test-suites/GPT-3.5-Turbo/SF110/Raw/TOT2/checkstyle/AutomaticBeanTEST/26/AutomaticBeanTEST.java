import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

public class AutomaticBeanTest {

    @Mock
    private Configuration configuration;

    @InjectMocks
    private AutomaticBean automaticBean;

    @Test
    public void testConfigure() throws CheckstyleException {
        // Typical use case
        automaticBean.configure(configuration);
        assertEquals(configuration, automaticBean.getConfiguration());

        // Edge case - empty configuration
        Configuration emptyConfig = mock(Configuration.class);
        when(emptyConfig.getAttributeNames()).thenReturn(new String[]{});
        when(emptyConfig.getChildren()).thenReturn(new Configuration[]{});
        automaticBean.configure(emptyConfig);
        assertNull(automaticBean.getConfiguration());

        // Error scenario - missing property key
        Configuration errorConfig = mock(Configuration.class);
        when(errorConfig.getAttributeNames()).thenReturn(new String[]{"key"});
        try {
            automaticBean.configure(errorConfig);
            fail("CheckstyleException not thrown");
        } catch (CheckstyleException e) {
            assertEquals("Property 'key' in module TestModule does not exist, please check the documentation", e.getMessage());
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        // Typical use case
        Context context = mock(Context.class);
        when(context.getAttributeNames()).thenReturn(List.of("key1", "key2"));
        when(context.get("key1")).thenReturn("value1");
        when(context.get("key2")).thenReturn("value2");
        automaticBean.contextualize(context);
        assertNotNull(automaticBean.getConfiguration());

        // Edge case - empty context
        Context emptyContext = mock(Context.class);
        when(emptyContext.getAttributeNames()).thenReturn(List.of());
        automaticBean.contextualize(emptyContext);
        assertNotNull(automaticBean.getConfiguration());

        // Error scenario - illegal value for property
        Context errorContext = mock(Context.class);
        when(errorContext.getAttributeNames()).thenReturn(List.of("key"));
        when(errorContext.get("key")).thenReturn(123);
        try {
            automaticBean.contextualize(errorContext);
            fail("CheckstyleException not thrown");
        } catch (CheckstyleException e) {
            assertEquals("illegal value '123' for property 'key' of module com.puppycrawl.tools.checkstyle.api.AutomaticBean", e.getMessage());
        }
    }

    @Test
    public void testFinishLocalSetup() {
        // No specific test cases for this method as it has no functionality to test
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        // Typical use case
        Configuration childConf = mock(Configuration.class);
        when(childConf.getName()).thenReturn("ChildConfig");
        automaticBean.setupChild(childConf);

        // Error scenario - non-null child configuration
        // An exception should be thrown
        Configuration nonNullChildConf = mock(Configuration.class);
        when(nonNullChildConf.getName()).thenReturn("NonNullChild");
        try {
            automaticBean.setupChild(nonNullChildConf);
            fail("CheckstyleException not thrown");
        } catch (CheckstyleException e) {
            assertEquals("NonNullChild is not allowed as a child in null", e.getMessage());
        }
    }

}