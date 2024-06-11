import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
        assertEquals("module$test", pcm.guessCJSModuleName("test"));
        assertEquals("module$common$js$module", pcm.guessCJSModuleName("common/js/module"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$test", ProcessCommonJSModules.toModuleName("test"));
        assertEquals("module$common$js$module", ProcessCommonJSModules.toModuleName("common/js/module"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$common$js$module", ProcessCommonJSModules.toModuleName("js/module", "common/"));
        assertEquals("module$common$module", ProcessCommonJSModules.toModuleName("../common/module", "js/"));
    }

    @Test
    public void testNormalizeSourceName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "src/");
        assertEquals("test", pcm.normalizeSourceName("src/test"));
        assertEquals("common/js/module", pcm.normalizeSourceName("src/common/js/module"));
    }

    // Add more test cases as needed for other public methods

}