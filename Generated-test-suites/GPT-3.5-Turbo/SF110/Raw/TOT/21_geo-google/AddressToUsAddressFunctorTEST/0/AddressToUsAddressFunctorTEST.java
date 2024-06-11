import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_withNullAddress_returnsNull() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(null);
        
        assertNull(result);
    }
    
    @Test
    public void testExecute_withEmptyAddress_returnsEmptyUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = new GeoAddress();
        
        GeoUsAddress result = functor.execute(address);
        
        assertNotNull(result);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertEquals(0, result.getAccuracy());
    }
    
    @Test
    public void testExecute_withFullValidAddress_returnsUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress address = createFullValidAddress();
        
        GeoUsAddress result = functor.execute(address);
        
        assertNotNull(result);
        assertEquals("US", result.getCountry());
        assertEquals("New York", result.getState());
        assertEquals("New York County", result.getCounty());
        assertEquals("New York City", result.getCity());
        assertEquals("10001", result.getPostalCode());
        assertEquals("123 Street", result.getAddressLine1());
        assertEquals("Apt 4B", result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertEquals(12.345, result.getCoordinate().getLatitude(), 0.001);
        assertEquals(67.89, result.getCoordinate().getLongitude(), 0.001);
        assertEquals(99.9, result.getAccuracy(), 0.001);
    }
    
    // Utility method to create a full valid address for testing
    private GeoAddress createFullValidAddress() {
        GeoAddress address = new GeoAddress();
        
        AddressDetails details = createAddressDetails();
        address.setAddressDetails(details);
        
        address.setCoordinate(new Coordinate(12.345, 67.89));
        address.setAccuracy(99.9);
        
        return address;
    }
    
    // Utility method to create address details for testing
    private AddressDetails createAddressDetails() {
        AddressDetails details = new AddressDetails();
        
        AddressDetails.Country country = new AddressDetails.Country();
        country.getCountryNameCode().add(new AddressDetails.Country.CountryNameCode("US"));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        adminArea.getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName("New York"));
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        subAdminArea.getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName("New York County"));
        
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        Locality locality = new Locality();
        locality.getLocalityName().add(new Locality.LocalityName("New York City"));
        
        PostalCode postalCode = new PostalCode();
        postalCode.getPostalCodeNumber().add(new PostalCode.PostalCodeNumber("10001"));
        
        locality.setPostalCode(postalCode);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.getThoroughfareName().add(new Thoroughfare.ThoroughfareName("123 Street"));
        thoroughfare.getThoroughfareName().add(new Thoroughfare.ThoroughfareName("Apt 4B"));
        
        locality.setThoroughfare(thoroughfare);
        
        adminArea.setLocality(locality);
        
        country.setAdministrativeArea(adminArea);
        details.setCountry(country);
        
        return details;
    }
}