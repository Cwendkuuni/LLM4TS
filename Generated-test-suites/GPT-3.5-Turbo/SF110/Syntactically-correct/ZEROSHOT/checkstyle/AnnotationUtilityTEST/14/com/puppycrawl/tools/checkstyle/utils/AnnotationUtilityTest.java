import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {
  
  private DetailAST ast;
  
  @Before
  public void setUp() {
    ast = new DetailAST();
  }
  
  @Test
  public void testContainsAnnotation() {
    assertFalse(AnnotationUtility.containsAnnotation(null, "Annotation"));
    
    ast.setType(155);
    assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
  }
  
  @Test
  public void testContainsAnnotationWithOnlyAstParameter() {
    assertFalse(AnnotationUtility.containsAnnotation(null));
    
    ast.setType(159);
    assertFalse(AnnotationUtility.containsAnnotation(ast));
    
    ast.setType(155);
    assertFalse(AnnotationUtility.containsAnnotation(ast));
    
    ast.setType(16);
    assertFalse(AnnotationUtility.containsAnnotation(ast));
    
    ast.setType(159);
    ast.addChild(new DetailAST());
    assertTrue(AnnotationUtility.containsAnnotation(ast));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    assertTrue(AnnotationUtility.containsAnnotation(ast));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    assertFalse(AnnotationUtility.containsAnnotation(ast));
  }
  
  @Test
  public void testGetAnnotationHolder() {
    assertNull(AnnotationUtility.getAnnotationHolder(null));
    
    ast.setType(159);
    assertNull(AnnotationUtility.getAnnotationHolder(ast));
    
    ast.setType(155);
    assertNull(AnnotationUtility.getAnnotationHolder(ast));
    
    ast.setType(16);
    assertNull(AnnotationUtility.getAnnotationHolder(ast));
    
    ast.setType(5);
    assertNull(AnnotationUtility.getAnnotationHolder(ast));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    assertEquals(158, AnnotationUtility.getAnnotationHolder(ast).getType());
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotationHolder(ast));
    assertEquals(158, AnnotationUtility.getAnnotationHolder(ast).getType());
  }
  
  @Test
  public void testGetAnnotation() {
    assertNull(AnnotationUtility.getAnnotation(null, "Annotation"));
    
    ast.setType(159);
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(5);
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    ast.getFirstChild().addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    ast.getFirstChild().addChild(new DetailAST());
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    ast.getFirstChild().addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    ast.getFirstChild().addChild(new DetailAST());
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    ast.getFirstChild().getNextSibling().addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    ast.getFirstChild().getNextSibling().addChild(new DetailAST());
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(155);
    ast.addChild(new DetailAST());
    ast.getFirstChild().getNextSibling().addChild(new DetailAST());
    assertNotNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
    
    ast.setType(16);
    ast.addChild(new DetailAST());
    ast.getFirstChild().getNextSibling().addChild(new DetailAST());
    assertNull(AnnotationUtility.getAnnotation(ast, "Annotation"));
  }
}