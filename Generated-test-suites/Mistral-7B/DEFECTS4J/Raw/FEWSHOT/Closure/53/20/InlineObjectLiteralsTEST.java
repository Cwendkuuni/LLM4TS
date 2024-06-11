package org.apache.commons.cli.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Compiler;
import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.DefaultCompiler;
import org.apache.commons.cli.ReferenceCollectingCallback.Behavior;
import org.apache.commons.cli.ReferenceCollectingCallback.Reference;
import org.apache.commons.cli.ReferenceCollectingCallback.ReferenceCollection;
import org.apache.commons.cli.ReferenceCollectingCallback.ReferenceMap;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.SourceCode;
import org.apache.commons.cli.test.util.TestUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() {
        Compiler compiler = new DefaultCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_inline_object_literal_" + System.currentTimeMillis();
         InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
         CompilationUnit cu = new CompilationUnit("TestInlineObjectLiterals.java");
         String testCode = TestUtils.getResourceAsString("/InlineObjectLiteralsTestCode.java");
         SourceCode sourceCode = new SourceCode(testCode, "TestInlineObjectLiterals.java");
         InlineObjectLiterals.InlineingBehavior behavior = new InlineObjectLiterals.InlineingBehavior();
         ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, behavior);
         inlineObjectLiterals.process(sourceCode, cu.getRootNode());
         ReferenceCollection referenceInfo = callback.getReferenceMap().getReferences(sourceCode.getVariable("obj"));
         assertNotNull(referenceInfo);
         assertEquals(0, referenceInfo.references.size());
         //Add more test cases here to cover all scenarios in InlineObjectLiterals class
    }

    @Test
    public void testInlineObjectLiteralsWithComplexReferences() {
        Compiler compiler = new DefaultCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_inline_object_literal_" + System.currentTimeMillis();
         InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
         CompilationUnit cu = new CompilationUnit("TestInlineObjectLiteralsWithComplexReferences.java");
         String testCode = TestUtils.getResourceAsString("/InlineObjectLiteralsTestCodeWithComplexReferences.java");
         SourceCode sourceCode = new SourceCode(testCode, "TestInlineObjectLiteralsWithComplexReferences.java");
         InlineObjectLiterals.InlineingBehavior behavior = new InlineObjectLiterals.InlineingBehavior();
         ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, behavior);
         inlineObjectLiterals.process(sourceCode, cu.getRootNode());
         ReferenceCollection referenceInfo = callback.getReferenceMap().getReferences(sourceCode.getVariable("objWithComplexReferences"));
         assertNotNull(referenceInfo);
         //Add assertions here to check if the inlined variables are correct
    }
}