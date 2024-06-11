package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public final class ScopeUtils
{
    public static Scope getScopeFromMods(final DetailAST aMods) {
        Scope returnValue = Scope.PACKAGE;
        for (AST token = aMods.getFirstChild(); token != null && returnValue == Scope.PACKAGE; token = token.getNextSibling()) {
            if ("public".equals(token.getText())) {
                returnValue = Scope.PUBLIC;
            }
            else if ("protected".equals(token.getText())) {
                returnValue = Scope.PROTECTED;
            }
            else if ("private".equals(token.getText())) {
                returnValue = Scope.PRIVATE;
            }
        }
        return returnValue;
    }
    
    public static Scope getSurroundingScope(final DetailAST node) {
        Scope returnValue = null;
        for (DetailAST token = node.getParent(); token != null; token = token.getParent()) {
            final int type = token.getType();
            if (type == 14 || type == 15 || type == 157 || type == 154) {
                final DetailAST mods = token.findFirstToken(5);
                final Scope modScope = getScopeFromMods(mods);
                if (returnValue == null || returnValue.isIn(modScope)) {
                    returnValue = modScope;
                }
            }
            else if (type == 136) {
                returnValue = Scope.ANONINNER;
                break;
            }
        }
        return returnValue;
    }
    
    public static boolean isInInterfaceBlock(final DetailAST node) {
        boolean returnValue = false;
        for (DetailAST token = node.getParent(); token != null && !returnValue; token = token.getParent()) {
            final int type = token.getType();
            if (type == 15) {
                returnValue = true;
            }
            else {
                if (type == 14 || type == 154 || type == 157) {
                    break;
                }
                if (type == 136) {
                    break;
                }
            }
        }
        return returnValue;
    }
    
    public static boolean isInAnnotationBlock(final DetailAST node) {
        boolean returnValue = false;
        for (DetailAST token = node.getParent(); token != null && !returnValue; token = token.getParent()) {
            final int type = token.getType();
            if (type == 157) {
                returnValue = true;
            }
            else {
                if (type == 14 || type == 154 || type == 15) {
                    break;
                }
                if (type == 136) {
                    break;
                }
            }
        }
        return returnValue;
    }
    
    public static boolean isInInterfaceOrAnnotationBlock(final DetailAST node) {
        return isInInterfaceBlock(node) || isInAnnotationBlock(node);
    }
    
    public static boolean isInEnumBlock(final DetailAST node) {
        boolean returnValue = false;
        for (DetailAST token = node.getParent(); token != null && !returnValue; token = token.getParent()) {
            final int type = token.getType();
            if (type == 154) {
                returnValue = true;
            }
            else {
                if (type == 15 || type == 157 || type == 14) {
                    break;
                }
                if (type == 136) {
                    break;
                }
            }
        }
        return returnValue;
    }
    
    public static boolean isInCodeBlock(final DetailAST node) {
        boolean returnValue = false;
        for (DetailAST token = node.getParent(); token != null; token = token.getParent()) {
            final int type = token.getType();
            if (type == 9 || type == 8 || type == 11 || type == 12) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }
    
    public static boolean isOuterMostType(final DetailAST node) {
        boolean returnValue = true;
        for (DetailAST parent = node.getParent(); parent != null; parent = parent.getParent()) {
            if (parent.getType() == 14 || parent.getType() == 15 || parent.getType() == 157 || parent.getType() == 154) {
                returnValue = false;
                break;
            }
        }
        return returnValue;
    }
    
    public static boolean isLocalVariableDef(final DetailAST node) {
        boolean localVariableDef = false;
        if (node.getType() == 10) {
            final DetailAST parent = node.getParent();
            final int type = parent.getType();
            localVariableDef = (type == 7 || type == 35 || type == 156);
        }
        if (node.getType() == 21) {
            final DetailAST parent = node.getParent();
            localVariableDef = (parent.getType() == 96);
        }
        return localVariableDef;
    }
    
    public static boolean isClassFieldDef(final DetailAST node) {
        return node.getType() == 10 && !isLocalVariableDef(node);
    }
    
    public static boolean isInScope(final DetailAST ast, final Scope scope) {
        final Scope surroundingScopeOfAstToken = getSurroundingScope(ast);
        return surroundingScopeOfAstToken == scope;
    }
}
