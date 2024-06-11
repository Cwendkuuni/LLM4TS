import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Resolver;
import com.google.javascript.rhino.IR;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Set up test environment
    Resolver resolver = new Resolver();
    AbstractCompiler compiler = new AbstractCompiler(resolver);
    IdGenerator idGenerator = new IdGenerator();
    CompilerPass crossModuleMethodMotion =
        new CrossModuleMethodMotion(compiler, idGenerator, true);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(crossModuleMethodMotion);

    // Test empty file
    Node emptyFile = Node.root();
    passManager.runPasses(emptyFile);
    assertNull(idGenerator.hasGeneratedAnyIds());

    // Test file with single module
    Node singleModuleFile = Node.objectLiteral();
    JSModule singleModule = new JSModule("singleModule", null, null);
    singleModuleFile.setLocal("singleModule", singleModule);
    passManager.runPasses(singleModuleFile);
    assertNull(idGenerator.hasGeneratedAnyIds());

    // Test file with two modules, each with a prototype method
    Node twoModulesFile = Node.objectLiteral();
    JSModule moduleA = new JSModule("moduleA", null, null);
    JSModule moduleB = new JSModule("moduleB", null, null);
    twoModulesFile.setLocal("moduleA", moduleA);
    twoModulesFile.setLocal("moduleB", moduleB);

    Node moduleAPrototype = Node.objectLiteral();
    moduleAPrototype.setLocal("prototype", Node.objectLiteral());
    moduleAPrototype.setPropertyIn("prototype", "methodA", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleA.setLocal("prototype", moduleAPrototype);

    Node moduleBPrototype = Node.objectLiteral();
    moduleBPrototype.setLocal("prototype", Node.objectLiteral());
    moduleBPrototype.setPropertyIn("prototype", "methodB", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleB.setLocal("prototype", moduleBPrototype);

    twoModulesFile.setPropertyIn("moduleA", "prototype", moduleAPrototype);
    twoModulesFile.setPropertyIn("moduleB", "prototype", moduleBPrototype);

    passManager.runPasses(twoModulesFile);
    assertTrue(idGenerator.hasGeneratedAnyIds());

    // Test file with two modules, each with a prototype method that depends
    // on each other
    Node twoModulesDependentFile = Node.objectLiteral();
    JSModule moduleC = new JSModule("moduleC", null, null);
    JSModule moduleD = new JSModule("moduleD", null, null);
    twoModulesDependentFile.setLocal("moduleC", moduleC);
    twoModulesDependentFile.setLocal("moduleD", moduleD);

    Node moduleCPrototype = Node.objectLiteral();
    moduleCPrototype.setLocal("prototype", Node.objectLiteral());
    moduleCPrototype.setPropertyIn("prototype", "methodC", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleC.setLocal("prototype", moduleCPrototype);

    Node moduleDPrototype = Node.objectLiteral();
    moduleDPrototype.setLocal("prototype", Node.objectLiteral());
    moduleDPrototype.setPropertyIn("prototype", "methodD", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleD.setLocal("prototype", moduleDPrototype);

    moduleCPrototype.setPropertyIn("prototype", "methodD", Node.getter(Node.string("methodD"), Node.undefined()));
    moduleDPrototype.setPropertyIn("prototype", "methodC", Node.setter(Node.string("methodC"), Node.undefined()));

    twoModulesDependentFile.setPropertyIn("moduleC", "prototype", moduleCPrototype);
    twoModulesDependentFile.setPropertyIn("moduleD", "prototype", moduleDPrototype);

    passManager.runPasses(twoModulesDependentFile);
    assertTrue(idGenerator.hasGeneratedAnyIds());

    // Test file with two modules, each with a prototype method that depends
    // on each other, and with a getter and a setter
    Node twoModulesDependentWithGetSetFile = Node.objectLiteral();
    JSModule moduleE = new JSModule("moduleE", null, null);
    JSModule moduleF = new JSModule("moduleF", null, null);
    twoModulesDependentWithGetSetFile.setLocal("moduleE", moduleE);
    twoModulesDependentWithGetSetFile.setLocal("moduleF", moduleF);

    Node moduleEPrototype = Node.objectLiteral();
    moduleEPrototype.setLocal("prototype", Node.objectLiteral());
    moduleEPrototype.setPropertyIn("prototype", "methodE", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleE.setLocal("prototype", moduleEPrototype);

    Node moduleFPrototype = Node.objectLiteral();
    moduleFPrototype.setLocal("prototype", Node.objectLiteral());
    moduleFPrototype.setPropertyIn("prototype", "methodF", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleF.setLocal("prototype", moduleFPrototype);

    moduleEPrototype.setPropertyIn("prototype", "methodF", Node.getter(Node.string("methodF"), Node.undefined()));
    moduleFPrototype.setPropertyIn("prototype", "methodE", Node.setter(Node.string("methodE"), Node.undefined()));

    twoModulesDependentWithGetSetFile.setPropertyIn("moduleE", "prototype", moduleEPrototype);
    twoModulesDependentWithGetSetFile.setPropertyIn("moduleF", "prototype", moduleFPrototype);

    passManager.runPasses(twoModulesDependentWithGetSetFile);
    assertTrue(idGenerator.hasGeneratedAnyIds());

    // Test file with two modules, each with a prototype method that depends
    // on each other, and with a getter, a setter and a read of closure variable
    Node twoModulesDependentWithClosureFile = Node.objectLiteral();
    JSModule moduleG = new JSModule("moduleG", null, null);
    JSModule moduleH = new JSModule("moduleH", null, null);
    twoModulesDependentWithClosureFile.setLocal("moduleG", moduleG);
    twoModulesDependentWithClosureFile.setLocal("moduleH", moduleH);

    Node moduleGPrototype = Node.objectLiteral();
    moduleGPrototype.setLocal("prototype", Node.objectLiteral());
    moduleGPrototype.setPropertyIn("prototype", "methodG", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleG.setLocal("prototype", moduleGPrototype);

    Node moduleHPrototype = Node.objectLiteral();
    moduleHPrototype.setLocal("prototype", Node.objectLiteral());
    moduleHPrototype.setPropertyIn("prototype", "methodH", Node.functionLiteral(Node.emptyStatementList(), Node.undefined()));
    moduleH.setLocal("prototype", moduleHPrototype);

    moduleGPrototype.setPropertyIn("prototype", "methodH", Node.getter(Node.string("methodH"), Node.undefined()));
    moduleHPrototype.setPropertyIn("prototype", "methodG", Node.setter(Node.string("methodG"), Node.undefined()));
    moduleGPrototype.setPropertyIn("prototype", "closure", Node.undefined());

    twoModulesDependentWithClosureFile.setPropertyIn("moduleG", "prototype", moduleGPrototype);
    twoModulesDependentWithClosureFile.setPropertyIn("moduleH", "prototype", moduleHPrototype);

    Node closure = Node.functionLiteral(Node.emptyStatementList(), Node.undefined());
    moduleGPrototype.setPropertyIn("prototype", "closure", closure);
    moduleHPrototype.setPropertyIn("prototype", "closure", closure);

    Node methodG = Node.getprop(Node.getprop(moduleGPrototype, "prototype"), "methodG");
    Node methodH = Node.getprop(Node.getprop(moduleHPrototype, "prototype"), "methodH");
    methodG.setChild(0, Node.getprop(Node.getprop(methodG, "closure"), "closure"));
    methodH.setChild(0, Node.getprop(Node.getprop(methodH, "closure"), "closure"));

    passManager.runPasses(twoModulesDependentWithClosureFile);
    assertFalse(idGenerator.hasGeneratedAnyIds());
  }
}