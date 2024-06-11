import static org.junit.Assert.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    // Test execute method
    @Test
    public void testExecute() {
        
        // Typical Use Case - Country and State exists
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode());
        addr.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
        addr.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getAdministrativeAreaName().get(0).setContent("California");
        GeoUsAddress result = functor.execute(addr);
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
        
        // Edge Case - No country name code
        addr.getAddressDetails().getCountry().getCountryNameCode().clear();
        result = functor.execute(addr);
        assertNull(result.getCountry());
        
        // Edge Case - No administrative area name
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode());
        addr.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
        addr.getAddressDetails().getCountry().getAdministrativeArea().getAdministrativeAreaName().clear();
        result = functor.execute(addr);
        assertNull(result.getState());
        
        // Error Scenario - Null address details
        addr.setAddressDetails(null);
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        
        // Error Scenario - Null country
        addr.setAddressDetails(new AddressDetails());
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        
        // Error Scenario - Null administrative area
        addr.getAddressDetails().setCountry(new AddressDetails.Country());
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        
        // Error Scenario - Null postal code
        addr.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        
        // Error Scenario - Null locality
        addr.getAddressDetails().getCountry().getAdministrativeArea().setLocality(null);
        result = functor.execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        
        // Error Scenario - Null thoroughfare
        addr.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        result = functor.execute(addr);
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }
}