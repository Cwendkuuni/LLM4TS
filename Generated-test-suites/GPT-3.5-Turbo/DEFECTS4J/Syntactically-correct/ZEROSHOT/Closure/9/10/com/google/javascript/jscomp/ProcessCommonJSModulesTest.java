import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".\\");
    }

    @After
    public void tearDown() {
        processCommonJSModules = null;
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = ".\\src\\test.js";
        String expected = "module$src$test";
        String actual = processCommonJSModules.guessCJSModuleName(filename);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName() {
        String filename = ".\\src\\test.js";
        String expected = "module$src$test";
        String actual = ProcessCommonJSModules.toModuleName(filename);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String requiredFilename = "./module.js";
        String currentFilename = ".\\src\\test.js";
        String expected = "module$src$module";
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        processCommonJSModules.process(externs, root);
    }

}