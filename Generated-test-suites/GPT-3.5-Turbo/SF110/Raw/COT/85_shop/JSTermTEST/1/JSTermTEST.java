
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class JSTermTest {
  
  @Test
  public void testJSTermConstructor() {
    JSTerm jsTerm = new JSTerm();
    assertNotNull(jsTerm);
  }
  
  @Test
  public void testJSTermParseList() {
    StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(System.in));
    JSTerm jsTerm = new JSTerm();
    JSTerm result = jsTerm.parseList(tokenizer);
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermToString() {
    JSTerm jsTerm = new JSTerm();
    jsTerm.addElement("test");
    String result = jsTerm.toString();
    assertEquals("test ", result);
  }
  
  @Test
  public void testJSTermCloneT() {
    JSTerm jsTerm = new JSTerm();
    JSTerm result = jsTerm.cloneT();
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermApplySubstitutionT() {
    JSTerm jsTerm = new JSTerm();
    JSSubstitution alpha = new JSSubstitution();
    JSTerm result = jsTerm.applySubstitutionT(alpha);
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermMatches() {
    JSTerm jsTerm = new JSTerm();
    JSTerm t = new JSTerm();
    JSSubstitution result = jsTerm.matches(t);
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermEquals() {
    JSTerm jsTerm = new JSTerm();
    JSTerm t = new JSTerm();
    boolean result = jsTerm.equals(t);
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermIsGround() {
    JSTerm jsTerm = new JSTerm();
    boolean result = jsTerm.isGround();
    assertEquals(true, result);
  }
  
  @Test
  public void testJSTermStandardizerTerm() {
    JSTerm jsTerm = new JSTerm();
    JSTerm result = jsTerm.standardizerTerm();
    assertNotNull(result);
  }
  
  @Test
  public void testJSTermCall() {
    JSTerm jsTerm = new JSTerm();
    JSTerm result = jsTerm.call();
    assertNotNull(result);
  }
}
