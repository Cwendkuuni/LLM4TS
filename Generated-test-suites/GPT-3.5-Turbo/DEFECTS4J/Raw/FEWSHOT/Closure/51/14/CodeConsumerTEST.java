package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {
    @Test
    public void testStatementNeedsEnded() {
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return 0;
            }
            public void append(String str) {}
        };
        
        consumer.statementNeedsEnded = true;
        consumer.endStatement(true);
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.statementNeedsEnded = false;
        consumer.statementStarted = true;
        consumer.endStatement();
        assertTrue(consumer.statementNeedsEnded);
        
        consumer.statementNeedsEnded = false;
        consumer.endStatement(true);
        assertFalse(consumer.statementNeedsEnded);
    }
    
    @Test
    public void testAdd() {
        StringBuilder strBuilder = new StringBuilder();
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return strBuilder.charAt(strBuilder.length() - 1);
            }
            public void append(String str) {
                strBuilder.append(str);
            }
        };
        
        consumer.statementStarted = true;
        
        consumer.add("word");
        assertEquals("word", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.add(" ");
        assertEquals("word ", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.add("word");
        assertEquals("word word", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.add("1");
        assertEquals("word word 1", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.add("+");
        assertEquals("word word 1+", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.add("+");
        assertEquals("word word 1+ +", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.statementStarted = false;
        consumer.add("word");
        assertEquals("word word 1+ + word", strBuilder.toString());
        assertTrue(consumer.statementNeedsEnded);
    }
    
    @Test
    public void testAddOp() {
        StringBuilder strBuilder = new StringBuilder();
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return strBuilder.charAt(strBuilder.length() - 1);
            }
            public void append(String str) {
                strBuilder.append(str);
            }
            public void appendOp(String op, boolean binOp) {
                strBuilder.append(op);
            }
            public void add(String newcode) {
                strBuilder.append(newcode);
            }
        };
        
        consumer.addOp("+", true);
        assertEquals("+", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.addOp("++", true);
        assertEquals("++", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.addOp("+", false);
        assertEquals("++ +", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.addOp("+", true);
        assertEquals("++ ++ +", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.statementStarted = true;
        consumer.addOp("--", true);
        assertEquals("++ ++ + --", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.addOp("-", false);
        assertEquals("++ ++ + -- -", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
        
        consumer.addOp(">", false);
        assertEquals("++ ++ + -- - >", strBuilder.toString());
        assertFalse(consumer.statementNeedsEnded);
    }
    
    @Test
    public void testAddNumber() {
        StringBuilder strBuilder = new StringBuilder();
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return strBuilder.charAt(strBuilder.length() - 1);
            }
            public void append(String str) {
                strBuilder.append(str);
            }
            public void add(String newcode) {
                strBuilder.append(newcode);
            }
        };
        
        consumer.addNumber(42);
        assertEquals("42", strBuilder.toString());
        
        strBuilder.setLength(0);
        consumer.statementStarted = true;
        consumer.addNumber(-42);
        assertEquals(" -42", strBuilder.toString());
    }
    
    @Test
    public void testIsWordChar() {
        assertFalse(CodeConsumer.isWordChar('*'));
        assertFalse(CodeConsumer.isWordChar('.'));
        assertFalse(CodeConsumer.isWordChar(','));
        assertFalse(CodeConsumer.isWordChar('<'));
        assertFalse(CodeConsumer.isWordChar('>'));
        assertFalse(CodeConsumer.isWordChar('('));
        assertFalse(CodeConsumer.isWordChar(')'));
        assertFalse(CodeConsumer.isWordChar('!'));
        assertFalse(CodeConsumer.isWordChar('='));
        assertFalse(CodeConsumer.isWordChar('$'));
        assertFalse(CodeConsumer.isWordChar('@'));
        
        assertTrue(CodeConsumer.isWordChar('0'));
        assertTrue(CodeConsumer.isWordChar('1'));
        assertTrue(CodeConsumer.isWordChar('2'));
        assertTrue(CodeConsumer.isWordChar('3'));
        assertTrue(CodeConsumer.isWordChar('4'));
        assertTrue(CodeConsumer.isWordChar('5'));
        assertTrue(CodeConsumer.isWordChar('6'));
        assertTrue(CodeConsumer.isWordChar('7'));
        assertTrue(CodeConsumer.isWordChar('8'));
        assertTrue(CodeConsumer.isWordChar('9'));
        assertTrue(CodeConsumer.isWordChar('a'));
        assertTrue(CodeConsumer.isWordChar('b'));
        assertTrue(CodeConsumer.isWordChar('c'));
        assertTrue(CodeConsumer.isWordChar('d'));
        assertTrue(CodeConsumer.isWordChar('e'));
        assertTrue(CodeConsumer.isWordChar('f'));
        assertTrue(CodeConsumer.isWordChar('g'));
        assertTrue(CodeConsumer.isWordChar('h'));
        assertTrue(CodeConsumer.isWordChar('i'));
        assertTrue(CodeConsumer.isWordChar('j'));
        assertTrue(CodeConsumer.isWordChar('k'));
        assertTrue(CodeConsumer.isWordChar('l'));
        assertTrue(CodeConsumer.isWordChar('m'));
        assertTrue(CodeConsumer.isWordChar('n'));
        assertTrue(CodeConsumer.isWordChar('o'));
        assertTrue(CodeConsumer.isWordChar('p'));
        assertTrue(CodeConsumer.isWordChar('q'));
        assertTrue(CodeConsumer.isWordChar('r'));
        assertTrue(CodeConsumer.isWordChar('s'));
        assertTrue(CodeConsumer.isWordChar('t'));
        assertTrue(CodeConsumer.isWordChar('u'));
        assertTrue(CodeConsumer.isWordChar('v'));
        assertTrue(CodeConsumer.isWordChar('w'));
        assertTrue(CodeConsumer.isWordChar('x'));
        assertTrue(CodeConsumer.isWordChar('y'));
        assertTrue(CodeConsumer.isWordChar('z'));
        assertTrue(CodeConsumer.isWordChar('A'));
        assertTrue(CodeConsumer.isWordChar('B'));
        assertTrue(CodeConsumer.isWordChar('C'));
        assertTrue(CodeConsumer.isWordChar('D'));
        assertTrue(CodeConsumer.isWordChar('E'));
        assertTrue(CodeConsumer.isWordChar('F'));
        assertTrue(CodeConsumer.isWordChar('G'));
        assertTrue(CodeConsumer.isWordChar('H'));
        assertTrue(CodeConsumer.isWordChar('I'));
        assertTrue(CodeConsumer.isWordChar('J'));
        assertTrue(CodeConsumer.isWordChar('K'));
        assertTrue(CodeConsumer.isWordChar('L'));
        assertTrue(CodeConsumer.isWordChar('M'));
        assertTrue(CodeConsumer.isWordChar('N'));
        assertTrue(CodeConsumer.isWordChar('O'));
        assertTrue(CodeConsumer.isWordChar('P'));
        assertTrue(CodeConsumer.isWordChar('Q'));
        assertTrue(CodeConsumer.isWordChar('R'));
        assertTrue(CodeConsumer.isWordChar('S'));
        assertTrue(CodeConsumer.isWordChar('T'));
        assertTrue(CodeConsumer.isWordChar('U'));
        assertTrue(CodeConsumer.isWordChar('V'));
        assertTrue(CodeConsumer.isWordChar('W'));
        assertTrue(CodeConsumer.isWordChar('X'));
        assertTrue(CodeConsumer.isWordChar('Y'));
        assertTrue(CodeConsumer.isWordChar('Z'));
        assertTrue(CodeConsumer.isWordChar('_'));
    }
    
    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return 0;
            }
            public void append(String str) {}
        };
        
        assertFalse(consumer.shouldPreserveExtraBlocks());
    }
    
    @Test
    public void testBreakAfterBlockFor() {
        CodeConsumer consumer = new CodeConsumer() {
            public char getLastChar() {
                return 0;
            }
            public void append(String str) {}
        };
        
        assertTrue(consumer.breakAfterBlockFor(null, true));
        assertFalse(consumer.breakAfterBlockFor(null, false));
    }
}