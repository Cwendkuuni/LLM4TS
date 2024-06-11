import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilityTest {

    private DetailAST ast;

    @Before
    public void setUp() {
        ast = new DetailAST();
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsAnnotationNullAST() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test
    public void containsAnnotationWithAnnotation() {
        ast.addChild(new DetailAST());
        ast.addChild(new DetailAST());
        ast.getFirstChild().addChild(new DetailAST(155));
        ast.getFirstChild().getChild(0).addChild(new DetailAST(159));
        ast.getFirstChild().getChild(0).getFirstChild().addChild(new DetailAST(123));
        ast.getFirstChild().getChild(0).getFirstChild().getNextSibling().addChild(new DetailAST(124));
        ast.getFirstChild().getChild(0).getFirstChild().getNextSibling().addChild(new DetailAST(125));

        boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");

        Assert.assertTrue(result);
    }

    @Test
    public void containsAnnotationWithoutAnnotation() {
        ast.addChild(new DetailAST());
        ast.addChild(new DetailAST());

        boolean result = AnnotationUtility.containsAnnotation(ast);

        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationHolderNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void getAnnotationHolderWithType155() {
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(155);
        ast.getChild(0).addChild(new DetailAST(158));

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        Assert.assertEquals(ast.getFirstChild().getFirstChild(), result);
    }

    @Test
    public void getAnnotationHolderWithType16() {
        ast.addChild(new DetailAST());
        ast.getChild(0).setType(16);
        ast.getChild(0).addChild(new DetailAST(158));

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        Assert.assertEquals(ast.getFirstChild().getFirstChild(), result);
    }

    @Test
    public void getAnnotationHolderWithOtherTypes() {
        ast.setType(10);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationNullAST() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationNullAnnotation() {
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnnotationEmptyOrSpacesAnnotation() {
        AnnotationUtility.getAnnotation(ast, "   ");
    }

    @Test
    public void getAnnotationWithMatchingAnnotation() {
        ast.addChild(new DetailAST());
        ast.addChild(new DetailAST());
        ast.getFirstChild().addChild(new DetailAST(155));
        ast.getFirstChild().getChild(0).addChild(new DetailAST(159));
        ast.getFirstChild().getChild(0).getFirstChild().addChild(new DetailAST(123));
        ast.getFirstChild().getChild(0).getFirstChild().getNextSibling().addChild(new DetailAST(124));
        ast.getFirstChild().getChild(0).getFirstChild().getNextSibling().addChild(new DetailAST(125));

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");

        Assert.assertNotNull(result);
        Assert.assertEquals(ast.getFirstChild().getChild(0), result);
    }

    @Test
    public void getAnnotationWithoutMatchingAnnotation() {
        ast.addChild(new DetailAST());
        ast.addChild(new DetailAST());

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");

        Assert.assertNull(result);
    }
}