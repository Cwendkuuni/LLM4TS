import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    private AddressToUsAddressFunctor functor;
    
    @Before
    public void setUp() {
        functor = AddressToUsAddressFunctor.getInstance();
    }
    
    @Test
    public void testExecute_validAddress_returnsGeoUsAddress() {
        // Arrange
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        country.getCountryNameCode().add(countryNameCode);
        countryNameCode.setContent("US");
        AddressDetails.Country.AdministrativeArea administrativeArea = new AddressDetails.Country.AdministrativeArea();
        country.setAdministrativeArea(administrativeArea);
        AddressDetails.Country.AdministrativeArea.AdministrativeAreaName administrativeAreaName = new AddressDetails.Country.AdministrativeArea.AdministrativeAreaName();
        administrativeArea.getAdministrativeAreaName().add(administrativeAreaName);
        administrativeAreaName.setContent("California");
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdministrativeArea = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdministrativeAreaName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdministrativeArea.getSubAdministrativeAreaName().add(subAdministrativeAreaName);
        subAdministrativeAreaName.setContent("Santa Clara");
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality locality = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality();
        subAdministrativeArea.setLocality(locality);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.LocalityName localityName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.LocalityName();
        locality.getLocalityName().add(localityName);
        localityName.setContent("San Jose");
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.PostalCode postalCode = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.PostalCode();
        locality.setPostalCode(postalCode);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.PostalCode.PostalCodeNumber postalCodeNumber = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.PostalCode.PostalCodeNumber();
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        postalCodeNumber.setContent("95131");
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare thoroughfare = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare.ThoroughfareNameType thoroughfareName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare.ThoroughfareNameType();
        thoroughfare.getThoroughfareName().add(thoroughfareName);
        thoroughfareName.setContent("1st Street");
        
        // Act
        GeoUsAddress result = functor.execute(address);
        
        // Assert
        assertEquals("US", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Santa Clara", result.getCounty());
        assertEquals("San Jose", result.getCity());
        assertEquals("95131", result.getPostalCode());
        assertEquals("1st Street", result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }
    
    @Test
    public void testExecute_missingCountry_returnsGeoUsAddressWithNullFields() {
        // Arrange
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        
        // Act
        GeoUsAddress result = functor.execute(address);
        
        // Assert
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }
    
    @Test
    public void testExecute_missingAdministrativeArea_returnsGeoUsAddressWithNullFields() {
        // Arrange
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        country.getCountryNameCode().add(countryNameCode);
        countryNameCode.setContent("US");
        
        // Act
        GeoUsAddress result = functor.execute(address);
        
        // Assert
        assertEquals("US", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
    }
    
    // More test cases can be added for other methods
    
}