import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

public class AutomaticBeanTest {

    @Test
    public void configureTest() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void contextualizeTest() {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        automaticBean.contextualize(context);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void finishLocalSetupTest() {
        AutomaticBean automaticBean = new AutomaticBean();
        assertDoesNotThrow(() -> automaticBean.finishLocalSetup());
    }

    @Test
    public void setupChildTest() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConf = new Configuration();
        assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConf));
    }

}