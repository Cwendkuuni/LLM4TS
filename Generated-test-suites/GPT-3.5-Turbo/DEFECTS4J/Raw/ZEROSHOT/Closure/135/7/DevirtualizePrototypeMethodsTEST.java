package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;

@RunWith(JUnit4.class)
public class DevirtualizePrototypeMethodsTest {
    private SimpleDefinitionFinder defFinder;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        defFinder = new SimpleDefinitionFinder(compiler);
        compiler = new AbstractCompiler();
    }

    @Test
    public void testRewriteDefinitionIfEligible_notEligible() {
        DefinitionSite defSite = new DefinitionSite();
        defSite.inExterns = false;
        defSite.inGlobalScope = false;

        rewriteDefinitionIfEligible(defSite, defFinder);

        // assert statements
    }

    @Test
    public void testRewriteDefinitionIfEligible_isEligible_notPrototypeMethodDefinition() {
        DefinitionSite defSite = new DefinitionSite();
        defSite.inExterns = false;
        defSite.inGlobalScope = true;

        rewriteDefinitionIfEligible(defSite, defFinder);

        // assert statements
    }

    @Test
    public void testRewriteDefinitionIfEligible_isEligible_multipleDefinitions() {
        DefinitionSite defSite = new DefinitionSite();
        defSite.inExterns = false;
        defSite.inGlobalScope = true;

        rewriteDefinitionIfEligible(defSite, defFinder);

        // assert statements
    }

    @Test
    public void testRewriteDefinitionIfEligible_isEligible_accessingPropertyInDifferentModule() {
        DefinitionSite defSite = new DefinitionSite();
        defSite.inExterns = false;
        defSite.inGlobalScope = true;

        rewriteDefinitionIfEligible(defSite, defFinder);

        // assert statements
    }

    @Test
    public void testRewriteCallSites() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        Definition definition = new Definition();
        String newMethodName = "newMethodName";

        rewriteCallSites(defFinder, definition, newMethodName);

        // assert statements
    }

    @Test
    public void testRewriteDefinition_duplicateCode() {
        Node node = new Node();
        String newMethodName = "newMethodName";

        rewriteDefinition(node, newMethodName);

        // assert statements
    }

    @Test
    public void testFixFunctionType() {
        Node functionNode = new Node();

        fixFunctionType(functionNode);

        // assert statements
    }

    @Test
    public void testReplaceReferencesToThis() {
        Node node = new Node();
        String name = "name";

        replaceReferencesToThis(node, name);

        // assert statements
    }
}