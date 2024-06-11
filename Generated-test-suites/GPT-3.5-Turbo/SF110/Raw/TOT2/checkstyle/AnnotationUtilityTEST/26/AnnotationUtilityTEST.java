import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));
        
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        AnnotationUtility.containsAnnotation(null, "MyAnnotation");
    }
    
    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }
    
    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST token158 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(155);
        when(ast.findFirstToken(158)).thenReturn(token158);
        
        assertEquals(token158, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test
    public void testGetAnnotationHolderWithTypeNot155() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST token5 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(10);
        when(ast.findFirstToken(5)).thenReturn(token5);
        
        assertEquals(token5, AnnotationUtility.getAnnotationHolder(ast));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        AnnotationUtility.getAnnotation(null, "MyAnnotation");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        
        AnnotationUtility.getAnnotation(ast, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        
        AnnotationUtility.getAnnotation(ast, "   ");
    }
    
    @Test
    public void testGetAnnotationWithValidAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST holder = mock(DetailAST.class);
        DetailAST child = mock(DetailAST.class);
        when(ast.findFirstToken(158)).thenReturn(holder);
        when(holder.getFirstChild()).thenReturn(child);
        when(child.getType()).thenReturn(159);
        when(child.getFirstChild()).thenReturn(mock(DetailAST.class));
        when(child.getFirstChild().getNextSibling()).thenReturn(mock(DetailAST.class));
        when(child.getFirstChild().getNextSibling().getText()).thenReturn("MyAnnotation");
        
        assertNotNull(AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }
}