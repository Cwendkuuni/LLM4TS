import org.junit.*;
import static org.junit.Assert.*;
import geo.google.datamodel.*;
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
    public void testExecute_withEmptyAddress_returnsEmptyGeoUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        GeoUsAddress result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
    }
    
    @Test
    public void testExecute_withValidAddress_returnsGeoUsAddress() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = createValidAddress();
        GeoUsAddress result = functor.execute(addr);
        assertNotNull(result);
        assertEquals("United States", result.getCountry());
        assertEquals("New York", result.getState());
        assertEquals("Queens", result.getCounty());
        assertEquals("New York City", result.getCity());
        assertEquals("11375", result.getPostalCode());
        assertEquals("123 Main St", result.getAddressLine1());
        assertEquals("Apt 4B", result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertEquals(0.123, result.getAccuracy(), 0.001);
    }
    
    // Helper method to create a valid address
    private GeoAddress createValidAddress() {
        GeoAddress addr = new GeoAddress();
        AddressDetails details = new AddressDetails();
        addr.setAddressDetails(details);
        
        AddressDetails.Country country = new AddressDetails.Country();
        details.setCountry(country);
        
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("New York");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("Queens");
        subAdminArea.getSubAdministrativeAreaName().add(subAdminAreaName);
        
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("New York City");
        locality.getLocalityName().add(localityName);
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("11375");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St");
        thoroughfare.getThoroughfareName().add(thoroughfareName);
        
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Apt 4B");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        
        Coordinate coordinate = new Coordinate();
        addr.setCoordinate(coordinate);
        
        addr.setAccuracy(0.123);
        
        return addr;
    }
}