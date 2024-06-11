package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        //Test 1: n.getType() == Token.FUNCTION
        //Test 2: jsDoc is constructor
        //Test 3: jsDoc hasThisType
        //Test 4: jsDoc isOverride
        //Test 5: pType == Token.BLOCK
        //Test 6: pType == Token.SCRIPT
        //Test 7: pType == Token.NAME
        //Test 8: pType == Token.ASSIGN
        //Test 9: parent != null && parent.getType() == Token.ASSIGN
        //Test 10: lhs.getType() == Token.GETPROP && lhs.getLastChild().getString().equals("prototype")
        //Test 11: lhs.getQualifiedName() != null && lhs.getQualifiedName().contains(".prototype.")
    }

    @Test
    public void testVisit() {
        //Test 1: n.getType() == Token.THIS && shouldReportThis(n, parent)
        //Test 2: n == assignLhsChild
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        //Test 1: jsDoc is null
        //Test 2: parentType == Token.NAME
        //Test 3: parentType == Token.ASSIGN
        //Test 4: jsDoc is null and parentType == Token.NAME
        //Test 5: parentType == Token.VAR
    }
}