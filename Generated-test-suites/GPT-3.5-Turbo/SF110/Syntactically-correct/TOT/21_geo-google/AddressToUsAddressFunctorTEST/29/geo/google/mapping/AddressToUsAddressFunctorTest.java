import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Typical use case - complete address
        GeoAddress addr = createCompleteAddress();
        GeoUsAddress ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("United States", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles County", ret.getCounty());
        assertEquals("Los Angeles", ret.getCity());
        assertEquals("90001", ret.getPostalCode());
        assertEquals("123 Main St.", ret.getAddressLine1());
        assertEquals("Apt 4B", ret.getAddressLine2());
        assertEquals(37.7749, ret.getCoordinate().getLatitude(), 0.0001);
        assertEquals(122.4194, ret.getCoordinate().getLongitude(), 0.0001);
        assertEquals(0.95, ret.getAccuracy(), 0.0001);
        
        // Edge case - no address details
        addr = new GeoAddress();
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCounty());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNull(ret.getCoordinate());
        assertEquals(0.0, ret.getAccuracy(), 0.0001);
        
        // Error scenario - null address
        addr = null;
        try {
            ret = functor.execute(addr);
            fail("Expected GeoException to be thrown");
        } catch (GeoException e) {
            assertEquals("Null address passed to functor", e.getMessage());
        }
    }
    
    private GeoAddress createCompleteAddress() {
        GeoAddress addr = new GeoAddress();
        AddressDetails details = new AddressDetails();
        
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("United States");
        countryNames.add(countryName);
        country.getCountryNameCode().addAll(countryNames);
        details.setCountry(country);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName name = new AdministrativeArea.AdministrativeAreaName();
        name.setContent("California");
        names.add(name);
        adminArea.getAdministrativeAreaName().addAll(names);
        country.setAdministrativeArea(adminArea);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName.setContent("Los Angeles County");
        subNames.add(subName);
        subAdminArea.getSubAdministrativeAreaName().addAll(subNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        Locality locality = new Locality();
        List<Locality.LocalityName> localNames = new ArrayList<>();
        Locality.LocalityName localName = new Locality.LocalityName();
        localName.setContent("Los Angeles");
        localNames.add(localName);
        locality.getLocalityName().addAll(localNames);
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        number.setContent("90001");
        numbers.add(number);
        postalCode.getPostalCodeNumber().addAll(numbers);
        locality.setPostalCode(postalCode);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St.");
        thoroughfares.add(thoroughfareName);
        thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Apt 4B");
        thoroughfares.add(thoroughfareName);
        thoroughfare.getThoroughfareName().addAll(thoroughfares);
        locality.setThoroughfare(thoroughfare);
        
        adminArea.setLocality(locality);
        
        addr.setAddressDetails(details);
        addr.setCoordinate(new Coordinate(37.7749, 122.4194));
        addr.setAccuracy(0.95);
        
        return addr;
    }
}