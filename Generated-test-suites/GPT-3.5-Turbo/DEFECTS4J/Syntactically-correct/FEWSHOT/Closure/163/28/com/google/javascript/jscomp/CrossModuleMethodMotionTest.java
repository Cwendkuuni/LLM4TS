import org.junit.Test;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    @Test
    public void testProcess() {
        // Test if there are more than 1 module
        idGenerator = new IdGenerator();
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);

        CompilerOptions options = new CompilerOptions();
        options.setWarningLevel(DiagnosticGroups.MISSING_PROPERTIES, CheckLevel.OFF);

        Compiler compiler = new Compiler();
        compiler.initOptions(options);

        Node externRoot = IR.root();
        Node root = IR.root();
        
        CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
        methodMotion.process(externRoot, root);
        
        // Test if there is 1 module
        JSModule module = new JSModule("module");
        moduleGraph.addModule(module);

        compiler = new Compiler();
        compiler.initOptions(options);

        externRoot = IR.root();
        root = IR.root();
        
        methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
        methodMotion.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        // TODO: Complete the test
    }
}