import org.junit.*;
import org.mockito.*;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = Mockito.spy(new AutomaticBean());
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration configuration = Mockito.mock(Configuration.class);

        // Test basic functionality
        automaticBean.configure(configuration);

        // Verify that the configuration is set
        Assert.assertEquals(configuration, automaticBean.getConfiguration());

        // Verify that tryCopyProperty method is called for each attribute
        Mockito.verify(automaticBean, Mockito.times(configuration.getAttributeNames().length))
                .tryCopyProperty(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(automaticBean, Mockito.times(configuration.getChildren().length))
                .setupChild(Mockito.any(Configuration.class));

        // Edge case: When the configuration is null
        try {
            automaticBean.configure(null);
            Assert.fail("CheckstyleException expected");
        } catch (CheckstyleException e) {
            Assert.assertEquals("Property 'null' in module null does not exist, please check the documentation", e.getMessage());
        }

        // Edge case: When the configuration has no attributes or children
        Configuration emptyConfig = Mockito.mock(Configuration.class);
        Mockito.when(emptyConfig.getAttributeNames()).thenReturn(new String[]{});
        Mockito.when(emptyConfig.getChildren()).thenReturn(new Configuration[]{});
        automaticBean.configure(emptyConfig);
        Mockito.verify(automaticBean, Mockito.times(0)).tryCopyProperty(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(automaticBean, Mockito.times(0)).setupChild(Mockito.any(Configuration.class));
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = Mockito.mock(Context.class);

        // Test basic functionality
        automaticBean.contextualize(context);

        // Verify that tryCopyProperty method is called for each attribute
        Mockito.verify(automaticBean, Mockito.times(context.getAttributeNames().size()))
                .tryCopyProperty(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyBoolean());

        // Edge case: When the context is null
        try {
            automaticBean.contextualize(null);
            Assert.fail("CheckstyleException expected");
        } catch (CheckstyleException e) {
            Assert.assertEquals("Property 'null' in module com.puppycrawl.tools.checkstyle.api.AutomaticBean does not exist, please check the documentation", e.getMessage());
        }

        // Edge case: When the context has no attributes
        Context emptyContext = Mockito.mock(Context.class);
        Mockito.when(emptyContext.getAttributeNames()).thenReturn(Mockito.mock(Collection.class));
        automaticBean.contextualize(emptyContext);
        Mockito.verify(automaticBean, Mockito.times(0)).tryCopyProperty(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyBoolean());
    }

    @Test
    public void testGetConfiguration() {
        // Test basic functionality
        Configuration configuration = automaticBean.getConfiguration();
        Assert.assertNull(configuration);
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        // Test basic functionality
        automaticBean.finishLocalSetup();
    }

    @Test
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = Mockito.mock(Configuration.class);

        // Test basic functionality
        automaticBean.setupChild(childConf);

        // Edge case: When the childConf is null
        try {
            automaticBean.setupChild(null);
            Assert.fail("CheckstyleException expected");
        } catch (CheckstyleException e) {
            Assert.assertEquals("null is not allowed as a child in null", e.getMessage());
        }
    }
}