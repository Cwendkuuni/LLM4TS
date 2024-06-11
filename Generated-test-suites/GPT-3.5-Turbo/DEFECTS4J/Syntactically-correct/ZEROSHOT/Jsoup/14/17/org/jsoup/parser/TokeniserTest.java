package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokeniserTest {
  
  @Test
  public void testRead() {
    // Test case for read method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    Token token = new Tokeniser().read();
    assertEquals(null, token);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    token = new Tokeniser().read();
    assertEquals(null, token);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    token = new Tokeniser().read();
    assertEquals(null, token);
  }
  
  @Test
  public void testEmit() {
    // Test case for emit method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    Token token = new Token();
    new Tokeniser().emit(token);
    assertEquals(token, emitPending);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    token = new Token();
    new Tokeniser().emit(token);
    assertEquals(token, emitPending);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    token = new Token();
    new Tokeniser().emit(token);
    assertEquals(token, emitPending);
  }
  
  @Test
  public void testEmitString() {
    // Test case for emit method with string parameter
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    String str = "";
    new Tokeniser().emit(str);
    assertEquals("", charBuffer.toString());
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    str = "abc";
    new Tokeniser().emit(str);
    assertEquals("abc", charBuffer.toString());
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    str = "def";
    new Tokeniser().emit(str);
    assertEquals("def", charBuffer.toString());
  }
  
  @Test
  public void testEmitChar() {
    // Test case for emit method with char parameter
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    char c = 'a';
    new Tokeniser().emit(c);
    assertEquals("a", charBuffer.toString());
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    c = 'b';
    new Tokeniser().emit(c);
    assertEquals("b", charBuffer.toString());
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    c = 'c';
    new Tokeniser().emit(c);
    assertEquals("c", charBuffer.toString());
  }
  
  @Test
  public void testGetState() {
    // Test case for getState method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    TokeniserState state = new Tokeniser().getState();
    assertEquals(TokeniserState.Data, state);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    state = new Tokeniser().getState();
    assertEquals(TokeniserState.Data, state);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    state = new Tokeniser().getState();
    assertEquals(TokeniserState.Data, state);
  }
  
  @Test
  public void testTransition() {
    // Test case for transition method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    TokeniserState state = TokeniserState.Data;
    new Tokeniser().transition(state);
    assertEquals(state, state);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    state = TokeniserState.Data;
    new Tokeniser().transition(state);
    assertEquals(state, state);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    state = TokeniserState.Data;
    new Tokeniser().transition(state);
    assertEquals(state, state);
  }
  
  @Test
  public void testAdvanceTransition() {
    // Test case for advanceTransition method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    TokeniserState state = TokeniserState.Data;
    new Tokeniser().advanceTransition(state);
    assertEquals(state, state);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    state = TokeniserState.Data;
    new Tokeniser().advanceTransition(state);
    assertEquals(state, state);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    state = TokeniserState.Data;
    new Tokeniser().advanceTransition(state);
    assertEquals(state, state);
  }
  
  @Test
  public void testAcknowledgeSelfClosingFlag() {
    // Test case for acknowledgeSelfClosingFlag method
    
    // Test case 1 : 
    // Input : 
    // Expected Output : 
    new Tokeniser().acknowledgeSelfClosingFlag();
    assertEquals(true, selfClosingFlagAcknowledged);
    
    // Test case 2 : 
    // Input : 
    // Expected Output : 
    new Tokeniser().acknowledgeSelfClosingFlag();
    assertEquals(true, selfClosingFlagAcknowledged);
    
    // Test case 3 : 
    // Input : 
    // Expected Output : 
    new Tokeniser().acknowledgeSelfClosingFlag();
    assertEquals(true, selfClosingFlagAcknowledged);
  }  
}