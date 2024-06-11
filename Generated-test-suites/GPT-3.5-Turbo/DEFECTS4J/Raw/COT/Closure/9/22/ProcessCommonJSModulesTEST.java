
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        assertEquals("module$file", pcm.guessCJSModuleName("file"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$file", ProcessCommonJSModules.toModuleName("file"));
        assertEquals("module$file", ProcessCommonJSModules.toModuleName("./file"));
        assertEquals("module$file", ProcessCommonJSModules.toModuleName("../file", "currentFile"));
    }
    
    @Test
    public void testProcess() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.process(null, null);
    }
    
    @Test
    public void testNormalizeSourceName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        assertEquals("file", pcm.normalizeSourceName(".//file"));
    }
    
    @Test
    public void testVisitRequireCall() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.visitRequireCall(null, null, null);
    }
    
    @Test
    public void testVisitScript() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.visitScript(null, null);
    }
    
    @Test
    public void testEmitOptionalModuleExportsOverride() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.emitOptionalModuleExportsOverride(null, null);
    }
    
    @Test
    public void testVisitModuleExports() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        pcm.visitModuleExports(null);
    }
    
    @Test
    public void testSuffixVarsCallback() {
        ProcessCommonJSModules.SuffixVarsCallback svc = new ProcessCommonJSModules().new SuffixVarsCallback("suffix");
        svc.visit(null, null, null);
    }
}
