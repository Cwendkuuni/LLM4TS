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
        GeoAddress addr = new GeoAddress();
        addr.setAccuracy(95);
        
        GeoUsAddress result = functor.execute(addr);
        
        // Test accuracy copied correctly
        assertEquals(95, result.getAccuracy());
    }
    
    // Add more test cases for other methods if needed
    
    // Edge case: Null Country
    @Test 
    public void testExecute_NullCountry() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress result = functor.execute(addr);
        
        assertNull(result.getCountry());
    }
    
    // Edge case: Multiple Country Names
    @Test
    public void testExecute_MultipleCountryNames() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode name1 = new AddressDetails.Country.CountryNameCode();
        name1.setContent("USA");
        AddressDetails.Country.CountryNameCode name2 = new AddressDetails.Country.CountryNameCode();
        name2.setContent("United States");
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(name1);
        countryNames.add(name2);
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress result = functor.execute(addr);
        
        assertEquals("USA", result.getCountry());
    }
    
}