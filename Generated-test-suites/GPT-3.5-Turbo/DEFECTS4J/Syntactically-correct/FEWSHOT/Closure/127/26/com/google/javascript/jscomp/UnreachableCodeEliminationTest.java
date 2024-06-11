package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.jscomp.graph.*;
import com.google.javascript.rhino.Node;

import java.util.logging.*;
import java.util.*;

public class UnreachableCodeEliminationTest {
    
    @Test
    public void testProcess1() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        Node externs = new Node(Token.AND, new Node(Token.TRUE), new Node(Token.FALSE));
        Node toplevel = new Node(Token.ASSIGN, new Node(Token.NAME, new String("x")), new Node(Token.TRUE));
        
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        uce.process(externs, toplevel);
        // verify the result
        // ...
    }
    
    @Test
    public void testProcess2() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        Node externs = new Node(Token.OR, new Node(Token.TRUE), new Node(Token.FALSE));
        Node toplevel = new Node(Token.AND, new Node(Token.THIS), new Node(Token.NULL));
        
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        uce.process(externs, toplevel);
        // verify the result
        // ...
    }
}