import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        String result = pcm.guessCJSModuleName("example.js");
        assertEquals("module$example", result);
    }

    @Test
    public void testToModuleName() {
        String result = ProcessCommonJSModules.toModuleName("./example.js");
        assertEquals("module$example", result);
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String result = ProcessCommonJSModules.toModuleName("./dependency.js", "example.js");
        assertEquals("module$dependency", result);
    }

    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.process(null, null);
    }
}