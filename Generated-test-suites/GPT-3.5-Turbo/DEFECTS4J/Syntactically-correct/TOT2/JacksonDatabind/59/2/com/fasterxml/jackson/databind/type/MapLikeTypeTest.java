import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(HashMap.class,
                new TypeBindings(), null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class),
                null, null, false);
    }

    @Test
    public void testGetKeyType() {
        assertEquals(SimpleType.construct(String.class), mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        assertEquals(SimpleType.construct(Integer.class), mapLikeType.getContentType());
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNonTypeBase() {
        JavaType baseType = Mockito.mock(JavaType.class);
        mapLikeType = MapLikeType.upgradeFrom(baseType,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(mapLikeType.equals(mapLikeType));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(mapLikeType.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentKey() {
        MapLikeType other = new MapLikeType(HashMap.class,
                new TypeBindings(), null, null,
                SimpleType.construct(Long.class), SimpleType.construct(Integer.class),
                null, null, false);
        assertFalse(mapLikeType.equals(other));
    }

    @Test
    public void testEqualsDifferentValueType() {
        MapLikeType other = new MapLikeType(HashMap.class,
                new TypeBindings(), null, null,
                SimpleType.construct(String.class), SimpleType.construct(Long.class),
                null, null, false);
        assertFalse(mapLikeType.equals(other));
    }

}