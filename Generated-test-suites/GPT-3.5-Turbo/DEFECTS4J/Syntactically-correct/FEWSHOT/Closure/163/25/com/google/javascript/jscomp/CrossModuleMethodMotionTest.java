package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;


public class CrossModuleMethodMotionTest {

    private AbstractCompiler compiler;
    private IdGenerator idGenerator;
    private CompilerPass pass;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    }

    @Test
    public void testProcess() {
        Node externRoot = new Node(IR.root());
        Node root = new Node(IR.root());
        pass.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_NullDeepestCommonModuleRef() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(null);
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_ReadsClosureVariables() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(true);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_NonFunctionValue() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.string("value")));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_DependsOnDeepestCommonModuleRef() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_ParentIsGetterDef() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        property.setParent(new Node(IR.getterDef()));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_ParentIsSetterDef() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        property.setParent(new Node(IR.setterDef()));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));
    }

    @Test
    public void testMoveMethods_NoIdGeneratorDeclarations() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));

        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_IdGeneratorHasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        idGenerator.newId();

        NameInfo nameInfo = new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        Symbol property = new Property();
        property.setValue(new Node(IR.function()));
        property.setPrototype(new Node(IR.name("prototype")));
        nameInfo.addDeclaration(property);
        nameInfo.setReadsClosureVariables(false);
        compiler.putMessage("JSC_INTERNAL_ERROR_MODULE_DEPEND", new DiagnosticType("error",
            "null deepest common module"));

        pass.moveMethods(Arrays.asList(nameInfo));

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

}