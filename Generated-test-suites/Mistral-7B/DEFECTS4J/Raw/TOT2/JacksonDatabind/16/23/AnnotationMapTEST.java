import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        Class<MyAnnotation> myAnnotationClass = MyAnnotation.class;
        annotationMap.add(myAnnotation);
        MyAnnotation returnedAnnotation = annotationMap.get(MyAnnotation.class);
        assertEquals(myAnnotation, returnedAnnotation);
    }
}