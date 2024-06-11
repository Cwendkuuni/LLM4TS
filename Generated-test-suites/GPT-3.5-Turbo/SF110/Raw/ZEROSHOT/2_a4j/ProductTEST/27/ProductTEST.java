import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testGetProduct() {
        FullProduct testProduct = product.getProduct("ASIN", "Offer", "Page");

        // Assert statements
        // ...
    }

}