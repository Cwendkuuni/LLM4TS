import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

    private ClosureCodingConvention closureCodingConvention;

    @Before
    public void setUp() {
        closureCodingConvention = new ClosureCodingConvention();
    }

    @Test
    public void testApplySubclassRelationship() {
        // Typical use case: type is INHERITS
        // superclass has prototype property
        FunctionType parentCtor = new FunctionType();
        FunctionType childCtor = new FunctionType();
        SubclassType type = SubclassType.INHERITS;
        closureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);
        // Assert that childCtor has "superClass_" property set to parentCtor's prototype
        assert(childCtor.getDeclaredProperties().containsKey("superClass_"));
        assert(childCtor.getPrototype().getDeclaredProperties().containsKey("constructor"));

        // Edge case: type is not INHERITS
        type = SubclassType.MIXIN;
        closureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);
        // Assert that childCtor does not have "superClass_" property set
        assert(!childCtor.getDeclaredProperties().containsKey("superClass_"));
    }

    @Test
    public void testGetclasssDefinedByCall() {
        // Typical use case: type is INHERITS
        Node callNode = new Node(Token.CALL);
        Node callName = new Node(Token.NAME, "inherits");
        Node subclass = new Node(Token.NAME, "SubClass");
        Node superclass = new Node(Token.NAME, "SuperClass");
        callNode.addChildToBack(callName);
        callName.addChildToBack(subclass);
        callName.addChildToBack(superclass);
        SubclassRelationship subclassRelationship = closureCodingConvention.getclasssDefinedByCall(callNode);
        // Assert that subclassRelationship has correct type, subclass and superclass nodes
        assert(subclassRelationship.getType() == SubclassType.INHERITS);
        assert(subclassRelationship.getSubclass() == subclass);
        assert(subclassRelationship.getSuperclass() == superclass);

        // Error scenario: subclass or superclass is not an unscoped qualified name
        subclass = new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.STRING, "b"));
        callName.addChildToBack(subclass);
        subclassRelationship = closureCodingConvention.getclasssDefinedByCall(callNode);
        // Assert that subclassRelationship is null
        assert(subclassRelationship == null);
    }

    @Test
    public void testIsSuperClassReference() {
        // Typical use case: propertyName is "superClass_"
        String propertyName = "superClass_";
        boolean isSuperClassReference = closureCodingConvention.isSuperClassReference(propertyName);
        // Assert that isSuperClassReference is true
        assert(isSuperClassReference);

        // Edge case: propertyName is not "superClass_"
        propertyName = "otherProperty";
        isSuperClassReference = closureCodingConvention.isSuperClassReference(propertyName);
        // Assert that isSuperClassReference is false
        assert(!isSuperClassReference);
    }
}