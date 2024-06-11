
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        try {
            bean.configure(config);
        } catch (CheckstyleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testContextualize() {
        AutomaticBean bean = new AutomaticBean();
        Context context = new Context();
        try {
            bean.contextualize(context);
        } catch (CheckstyleException e) {
            e.printStackTrace();
        }
    }
}
