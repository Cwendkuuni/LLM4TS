package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = new Node(Token.SCRIPT);
        
        collapseVariableDeclarations.process(externs, root);
        
        // Add assertions here
        
        root.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "var1"))));
        root.addChildToBack(new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.STRING, "var2")),
                                         new Node(Token.TRUE)) );
                                         
        collapseVariableDeclarations.process(externs, root);
        
        // Add assertions here
        
        root.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING, "var3")),
                                          new Node(Token.TRUE)));
                                          
        collapseVariableDeclarations.process(externs, root);
        
        // Add assertions here
    }
}