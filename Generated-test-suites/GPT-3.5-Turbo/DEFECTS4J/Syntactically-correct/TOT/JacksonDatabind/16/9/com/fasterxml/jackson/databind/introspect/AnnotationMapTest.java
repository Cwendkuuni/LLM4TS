import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    // Test setup
    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    // Test for public <A extends Annotation> A get(Class<A> cls)
    @Test
    public void testGetAnnotation() {
        // 1. Test case: Annotation exists in the map
        // Add an annotation to the map
        MyAnnotation myAnnotation = new MyAnnotationImpl();
        annotationMap.add(myAnnotation);

        // Retrieve the annotation using get()
        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);

        // Assertion: The retrieved annotation should not be null
        assertNotNull(retrievedAnnotation);

        // Assertion: The retrieved annotation should be the same as the one added
        assertSame(myAnnotation, retrievedAnnotation);

        // 2. Test case: Annotation does not exist in the map
        // Retrieve an annotation that does not exist in the map
        MyAnnotation nonExistingAnnotation = annotationMap.get(NonExistingAnnotation.class);

        // Assertion: The retrieved annotation should be null
        assertNull(nonExistingAnnotation);
    }

    // Test for public Iterable<Annotation> annotations()
    @Test
    public void testGetAnnotations() {
        // 1. Test case: Map is empty
        // Retrieve annotations from an empty map
        Iterable<Annotation> emptyAnnotations = annotationMap.annotations();

        // Assertion: The Iterable should be empty
        assertFalse(emptyAnnotations.iterator().hasNext());

        // 2. Test case: Map contains annotations
        // Add some annotations to the map
        MyAnnotation annotation1 = new MyAnnotationImpl();
        annotationMap.add(annotation1);
        MyAnnotation annotation2 = new MyAnnotationImpl();
        annotationMap.add(annotation2);

        // Retrieve annotations from the map
        Iterable<Annotation> retrievedAnnotations = annotationMap.annotations();

        // Assertion: The Iterable should contain exactly the annotations added
        assertEquals(2, ((List<Annotation>) retrievedAnnotations).size());
        assertTrue(((List<Annotation>) retrievedAnnotations).contains(annotation1));
        assertTrue(((List<Annotation>) retrievedAnnotations).contains(annotation2));
    }

    // Test for public static AnnotationMap merge(AnnotationMap primary, AnnotationMap secondary)
    @Test
    public void testMergeAnnotations() {
        // 1. Test case: Both maps are empty
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        // Merge the maps
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        // Assertion: The merged map should be empty
        assertEquals(0, merged.size());

        // 2. Test case: One map is empty, the other contains annotations
        primary.add(new MyAnnotationImpl());
        primary.add(new OtherAnnotationImpl());

        // Merge the maps
        AnnotationMap merged2 = AnnotationMap.merge(primary, secondary);

        // Assertion: The merged map should contain the annotations from the non-empty map
        assertEquals(2, merged2.size());

        // 3. Test case: Both maps contain annotations with different types
        primary = new AnnotationMap();
        primary.add(new MyAnnotationImpl());
        secondary = new AnnotationMap();
        secondary.add(new OtherAnnotationImpl());

        // Merge the maps
        AnnotationMap merged3 = AnnotationMap.merge(primary, secondary);

        // Assertion: The merged map should contain all the annotations from both maps
        assertEquals(2, merged3.size());

        // 4. Test case: Both maps contain annotations with the same type
        primary = new AnnotationMap();
        MyAnnotation duplicateAnnotation = new MyAnnotationImpl();
        primary.add(duplicateAnnotation);
        secondary = new AnnotationMap();
        secondary.add(duplicateAnnotation);

        // Merge the maps
        AnnotationMap merged4 = AnnotationMap.merge(primary, secondary);

        // Assertion: The merged map should contain only one instance of the duplicated annotation
        assertEquals(1, merged4.size());
    }

    // Test for public boolean addIfNotPresent(Annotation ann)
    @Test
    public void testAddIfNotPresent() {
        // 1. Test case: Annotation does not exist in the map
        // Create a new annotation
        MyAnnotation annotation = new MyAnnotationImpl();

        // Add the annotation to the map using addIfNotPresent()
        boolean added = annotationMap.addIfNotPresent(annotation);

        // Assertion: The annotation should have been added
        assertTrue(added);

        // 2. Test case: Annotation already exists in the map
        // Add an annotation to the map
        annotationMap.add(annotation);

        // Add the same annotation to the map using addIfNotPresent()
        boolean added2 = annotationMap.addIfNotPresent(annotation);

        // Assertion: The annotation should not have been added
        assertFalse(added2);
    }

    // Test for public boolean add(Annotation ann)
    @Test
    public void testAddAnnotation() {
        // 1. Test case: Annotation does not exist in the map
        // Create a new annotation
        MyAnnotation annotation = new MyAnnotationImpl();

        // Add the annotation to the map using add()
        boolean added = annotationMap.add(annotation);

        // Assertion: The annotation should have been added
        assertTrue(added);

        // 2. Test case: Annotation already exists in the map
        // Add the same annotation to the map using add()
        boolean added2 = annotationMap.add(annotation);

        // Assertion: The annotation should have been added (duplicates allowed)
        assertTrue(added2);
    }

    // Test for public int size()
    @Test
    public void testSize() {
        // 1. Test case: Map is empty
        // Assertion: The size of the map should be 0
        assertEquals(0, annotationMap.size());

        // 2. Test case: Map contains annotations
        // Add some annotations to the map
        annotationMap.add(new MyAnnotationImpl());
        annotationMap.add(new OtherAnnotationImpl());

        // Assertion: The size of the map should be equal to the number of annotations added
        assertEquals(2, annotationMap.size());
    }

    // Test for public String toString()
    @Test
    public void testToString() {
        // 1. Test case: Map is empty
        // Assertion: The toString() method should return "[null]"
        assertEquals("[null]", annotationMap.toString());

        // 2. Test case: Map contains annotations
        // Add some annotations to the map
        MyAnnotation annotation1 = new MyAnnotationImpl();
        annotationMap.add(annotation1);
        MyAnnotation annotation2 = new MyAnnotationImpl();
        annotationMap.add(annotation2);

        // Retrieve the string representation of the map
        String mapString = annotationMap.toString();

        // Assertion: The string should contain the string representations of the added annotations
        assertTrue(mapString.contains(annotation1.toString()));
        assertTrue(mapString.contains(annotation2.toString()));
    }

    // Custom annotations for testing
    public interface MyAnnotation extends Annotation {
    }

    public interface NonExistingAnnotation extends Annotation {
    }

    public interface OtherAnnotation extends Annotation {
    }

    public class MyAnnotationImpl implements MyAnnotation {
    }

    public class OtherAnnotationImpl implements OtherAnnotation {
    }

}