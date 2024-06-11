import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        String filename = "test.js";
        String expectedModuleName = "module$test";
        String moduleName = pcm.guessCJSModuleName(filename);
        assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String expectedModuleName = "module$test";
        String moduleName = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./directory/test.js";
        String currentFilename = "/root/current.js";
        String expectedModuleName = "module$root$directory$test";
        String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expectedModuleName, moduleName);
    }

    @Test
    public void testNormalizeSourceName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "path/");
        String filename = "path/file.js";
        String expectedNormalizedName = "file.js";
        String normalizedName = pcm.normalizeSourceName(filename);
        assertEquals(expectedNormalizedName, normalizedName);
    }
}