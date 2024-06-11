import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class ProductTest {

  private Product product;
  
  @Before
  public void setUp() {
    product = new Product();
  }

  @Test
  public void testGetProduct_withValidInputs_shouldReturnFullProductObject() {
    FullProduct fullProduct = product.getProduct("ASIN123", "Offer123", "Page123");
    assertNotNull(fullProduct);
  }

  @Test
  public void testGetProduct_withNullInputs_shouldReturnNull() {
    FullProduct fullProduct = product.getProduct(null, null, null);
    assertNull(fullProduct);
  }

  @Test
  public void testGetProduct_withEmptyInputs_shouldReturnNull() {
    FullProduct fullProduct = product.getProduct("", "", "");
    assertNull(fullProduct);
  }

  @Test
  public void testGetProduct_withNonexistentFile_shouldReturnNull() {
    FullProduct fullProduct = product.getProduct("Nonexistent", "Offer123", "Page123");
    assertNull(fullProduct);
  }

  @Test
  public void testGetProduct_withProductWithoutDetails_shouldReturnFullProductWithoutDetails() {
    FullProduct fullProduct = product.getProduct("ASINWithoutDetails", "Offer123", "Page123");
    assertNotNull(fullProduct);
    assertNull(fullProduct.getDetails());
  }

  @Test
  public void testGetProduct_withProductWithoutAccessories_shouldReturnFullProductWithoutAccessories() {
    FullProduct fullProduct = product.getProduct("ASINWithoutAccessories", "Offer123", "Page123");
    assertNotNull(fullProduct);
    assertNotNull(fullProduct.getDetails());
    assertNull(fullProduct.getDetails().getAccessories());
  }

  @Test
  public void testGetProduct_withProductWithoutAccessoryArray_shouldReturnFullProductWithEmptyAccessoryArray() {
    FullProduct fullProduct = product.getProduct("ASINWithoutAccessoryArray", "Offer123", "Page123");
    assertNotNull(fullProduct);
    assertNotNull(fullProduct.getDetails());
    assertNotNull(fullProduct.getDetails().getAccessories());
    assertNotNull(fullProduct.getDetails().getAccessories().getAccessoryArray());
    assertEquals(0, fullProduct.getDetails().getAccessories().getAccessoryArray().size());
  }

  @Test
  public void testGetProduct_withProductWithValidAccessories_shouldReturnFullProductWithAccessories() {
    FullProduct fullProduct = product.getProduct("ASINWithAccessories", "Offer123", "Page123");
    assertNotNull(fullProduct);
    assertNotNull(fullProduct.getDetails());
    assertNotNull(fullProduct.getDetails().getAccessories());
    assertNotNull(fullProduct.getDetails().getAccessories().getAccessoryArray());
    assertTrue(fullProduct.getDetails().getAccessories().getAccessoryArray().size() > 0);
  }

  @Test
  public void testGetProduct_withValidProductInfo_shouldReturnFullProductWithMiniProducts() {
    FullProduct fullProduct = product.getProduct("ASINWithProductInfo", "Offer123", "Page123");
    assertNotNull(fullProduct);
    assertNotNull(fullProduct.getSimilarItems());
    assertTrue(fullProduct.getSimilarItems().size() > 0);
  }

}