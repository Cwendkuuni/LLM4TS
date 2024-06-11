import static org.junit.Assert.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testGetInstance() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        assertNotNull(functor);
    }
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Typical use case
        GeoAddress geoAddress = new GeoAddress();
        GeoUsAddress usAddress = functor.execute(geoAddress);
        assertNotNull(usAddress);
        
        // Edge case: No country details
        geoAddress.setAddressDetails(new AddressDetails());
        usAddress = functor.execute(geoAddress);
        assertNotNull(usAddress);
        
        // Edge case: Multiple country names
        AddressDetails.Country country = new AddressDetails.Country();
        country.addCountryNameCode(new AddressDetails.Country.CountryNameCode("US"));
        country.addCountryNameCode(new AddressDetails.Country.CountryNameCode("USA"));
        geoAddress.getAddressDetails().setCountry(country);
        usAddress = functor.execute(geoAddress);
        assertEquals("US", usAddress.getCountry());
        
        // Error scenario: Null input
        try {
            usAddress = functor.execute(null);
            fail("Expected GeoException not thrown");
        } catch (GeoException e) {
            // Expected GeoException
        }
    }
}